package analyse;

import org.joda.time.DateTime;
import org.joda.time.Days;

import others.Mediator;

import recommend.Recommend;
import recommend.WalkingRecommend;
import android.content.Context;
import android.widget.Toast;


public class WalkingAnalyse implements Analyse{

	private static float improvementPercentage = 90;
	private static float averagePercentage = 60;
	
	Context context;
	Mediator mediator;
	Recommend recommend;
	
	boolean isFirstActivity;
	float lastRecommendation;
	float lastMonitor;
	float lastActivity;
	
	
	public WalkingAnalyse(Context context){
		this.context = context;
		mediator = new Mediator(context);
		recommend = new WalkingRecommend(context);
		isFirstActivity = !(mediator.setWalkingHistory());
	}

	
	@Override
	public void analyse() {
		if(isFirstActivity){
			//send recommendation for the very first activity
			recommend.maintainRecommendation();
		}
		else{
			lastRecommendation = Float.parseFloat(mediator.recommendationHistory.get(mediator.recommendationHistory.size()-1));
			
			if(monitorResult>=improvementPercentage){
				//increase
				recommend.increaseRecommendation();
			}
			if(monitorResult>=averagePercentage && monitorResult<improvementPercentage){
				//stay the same
				recommend.maintainRecommendation();
			}
			if(monitorResult<averagePercentage){
				//decrease
				recommend.decreaseRecommendation();
			}
		}
		
	}
	
	
	DateTime getLastDate(){
		DateTime lastDate = new DateTime(mediator.activityDateHistory.get(mediator.activityDateHistory.size()-1));
		return lastDate;
	}
	
	DateTime getCurrentDate(){
		return new DateTime();
	}
	
	boolean isSameDay(){
		DateTime today = new DateTime();
		if(Days.daysBetween(getLastDate(), today).getDays() == 0){
			return true;
		}
		return false;
	}

	//this is the function called whenever the user enters his activity (through the graphic interface)
	float time, distance;
	float monitorResult;
	@Override
	public void enterActivity(float time, float distance, float calories) {
		this.time =time;
		this.distance =distance;
		String date = getCurrentDate().toString().substring(0, 10);
		try{
		    Toast.makeText(context, "Number of entries: " + mediator.getActivityDistanceHistory().size(), Toast.LENGTH_SHORT).show();
			if(!isSameDay()){
				monitorResult = Float.parseFloat(mediator.monitorWalker(time, distance));			 
				analyse();
				mediator.buildWalkerEntry(recommend.recommend(context), date, Float.toString(distance), Float.toString(time), Float.toString(calories));
			}
			else{
				//(should I keep using the updated recommendation?? For now, yes.)
				distance += Float.parseFloat(mediator.getLastActivityDistance());
				time += Float.parseFloat(mediator.getLastActivityTime());
				monitorResult = Float.parseFloat(mediator.monitorWalker(time, distance));
				analyse();
				mediator.updateWalkerEntry(recommend.recommend(context),date, Float.toString(distance), Float.toString(time));
			} 
		}
		catch(ArrayIndexOutOfBoundsException e){
			monitorResult = Float.parseFloat(mediator.monitorWalker(time, distance));
			analyse();
			mediator.buildWalkerEntry(recommend.recommend(context), date, Float.toString(distance), Float.toString(time), Float.toString(calories));
		}
	}


	@Override
	public void updateActivity(float time, float distance, float calories) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getMonitorResult() {
		return mediator.getMonitorResult();
		
	}


	@Override
	public void enterCalories(float calories) {
		// TODO Auto-generated method stub
		
	}
	
	
}
