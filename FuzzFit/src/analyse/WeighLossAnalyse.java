package analyse;

import org.joda.time.DateTime;
import org.joda.time.Days;

import others.Mediator;
import others.User;

import recommend.Recommend;
import recommend.WalkingRecommend;
import recommend.WeightLossRecommend;
import android.content.Context;


public class WeighLossAnalyse implements Analyse{

	private static float improvementPercentage = 90;
	private static float averagePercentage = 60;
	
	Context context;
	Mediator mediator;
	Recommend recommend;
	User user;
	
	boolean isFirstActivity;
	float lastRecommendation;
	float lastMonitor;
	float lastActivity;
	
	float calories;
	
	public WeighLossAnalyse(Context context){
		this.context = context;
		mediator = new Mediator(context);
		recommend = new WeightLossRecommend();
		user = new User(context);
		isFirstActivity = !(mediator.setWalkingHistory());
	}
	
	float monitorResult;
	@Override
	public void analyse() {
		// TODO Auto-generated method stub
		if(isFirstActivity){
			//send recommendation for the very first activity
			recommend.maintainRecommendation();
		}
		else{
			lastRecommendation = Float.parseFloat(mediator.recommendationHistory.get(mediator.recommendationHistory.size()-1));
			//lastMonitor = Float.parseFloat(mediator.getMonitorResult());//this is not updating!!! 
		//	lastMonitor = Float.parseFloat(mediator.monitorWalker(this.time, this.distance));
			
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

	@Override
	public void enterActivity(float time, float distance, float velocity) {
		if(velocity == 0){
			velocity = distance/(time/60); //we need to know the average velocity in order to find the calories
		}
		calculateCalories(time, velocity);
		String date = getCurrentDate().toString().substring(0, 10);
		try{
			if(!isSameDay()){
				//monitorResult = Float.parseFloat(mediator.monitorWalker(time, distance));	MAKE ONE FOR WEIGHT LOSS
				analyse();
				//MAKE ONE FOR WEIGHT LOSS:
				//mediator.buildWalkerEntry(recommend.recommend(context), date, Float.toString(distance), Float.toString(time));
			}
			else{
				//MAKE THE COMMENTED ONES FOR WEIGHT LOSS!! 
				//monitorResult = Float.parseFloat(mediator.monitorWalker(time, distance));
				analyse();
				//mediator.buildWalkerEntry(recommend.recommend(context), date, Float.toString(distance), Float.toString(time));
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		
	}

	@Override
	public void updateActivity(float time, float distance, float velocity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMonitorResult() {
		// TODO Auto-generated method stub
		return null;
	}


	
	void calculateCalories(float time, float velocity){
	//(use the calorie class)
	//	float met = defineMET(velocity); 
	//	calories = Float.parseFloat(user.getWeight())*(time/60)*met;
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

	
}
