package analyse;

import org.joda.time.DateTime;
import org.joda.time.Days;

import others.CalorieHandler;
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
	
	CalorieHandler calorieHandler;
	
	boolean isFirstActivity;
	float lastRecommendation;
	float lastMonitor;
	float lastActivity;
	
	float calories;
	
	public WeighLossAnalyse(Context context){
		this.context = context;
		mediator = new Mediator(context);
		recommend = new WeightLossRecommend(context);
		user = new User(context);
		isFirstActivity = !(mediator.setWalkingHistory());
	}
	
	float monitorResult;
	@Override
	public void analyse() {
		if(isFirstActivity){
			//send recommendation for the very first activity
			recommend.maintainRecommendation();
		}
		else{
			lastRecommendation = Float.parseFloat(mediator.recommendationHistory.get(mediator.recommendationHistory.size()-1));
		
			//we don't go over 500 because it may be unhealthy
			if(monitorResult>=improvementPercentage && lastRecommendation<500){
				//increase
				recommend.increaseRecommendation();
			}
			else{
				recommend.maintainRecommendation();
			}
//			if(monitorResult>=averagePercentage && monitorResult<improvementPercentage){
//				//stay the same
//				recommend.maintainRecommendation();
//			}
//			if(monitorResult<averagePercentage){
//				//decrease
//				recommend.decreaseRecommendation();
//			}
		}
	}

	@Override
	public void enterActivity(float time, float distance, float calories) {
		String date = getCurrentDate().toString().substring(0, 10);
		try{

			if(!isSameDay()){
				monitorResult = Float.parseFloat(mediator.monitorWeightLoss(calories));
				analyse();
				mediator.buildWeightLossEntry(recommend.recommend(context), date, Float.toString(distance), Float.toString(time), Float.toString(calories));
			}
			else{
				distance += Float.parseFloat(mediator.getLastActivityDistance());
				time += Float.parseFloat(mediator.getLastActivityTime());
				calories += Float.parseFloat(mediator.getLastActivityCalories());
				monitorResult = Float.parseFloat(mediator.monitorWeightLoss(calories));
				analyse();
				mediator.updateWeightLossEntry(recommend.recommend(context), date, Float.toString(distance), Float.toString(time),Float.toString(calories));
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			monitorResult = Float.parseFloat(mediator.monitorWeightLoss(calories));
			analyse();
			mediator.buildWeightLossEntry(recommend.recommend(context), date, Float.toString(distance), Float.toString(time), Float.toString(calories));
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

	//enter the amount of calories the user consumes
	@Override
	public void enterCalories(float calories) {

	}

	
}
