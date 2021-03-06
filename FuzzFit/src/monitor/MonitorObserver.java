package monitor;


import others.User;

import activities.WeightLossActivity;
import android.content.Context;
import history.ActivityHistory;
import history.History;


public class MonitorObserver {
	
	//walk
	public static final int dayFactor = 7;
	public static final float minimumWeeklyDistance = 5500;
	public static final float minimumDailyDistance = minimumWeeklyDistance/dayFactor;	
	public static float currentDailyDistance = minimumDailyDistance;

	public static final float minimumDailyTime = 30;
	public static float currentDailyTime = minimumDailyTime;
	
	public static void updateWalk(Context context){
		History history = new History(context);
		if(!history.isWalkerEmpty())
			currentDailyDistance = Float.parseFloat(history.getWalkerHistory().getRecommendation().get(history.getWalkerHistory().getRecommendation().size()-1));
		else
			currentDailyDistance = minimumDailyDistance;
	}
	
	//running
	
	public static float averageHumanSpeed = 0; // 2.9m/s woman or 3.8m/s men 
	public static float currentRunningDistance=0;
	public static float currentRunningTime=0;
	
	public static void updateRun(Context context){
		User user = new User(context); //error happening here! (05/05/2014)
		History history = new History(context);
		
		//we also need to find the necessary distance.
		averageHumanSpeed = user.getAverageRunningSpeed();
		
		if(!history.isRunnerEmpty()){
			currentRunningTime = Float.parseFloat(history.getRunnerHistory().getRecommendation().get(history.getRunnerHistory().getRecommendation().size()-1)); //minutes
			currentRunningDistance = averageHumanSpeed*(currentRunningTime*60);
		}
		else{
			currentRunningTime = 10;
			currentRunningDistance = averageHumanSpeed*(currentRunningTime*60);
		}
	}
	
	//weight loss related:
	public static float calories = WeightLossActivity.caloriesToLose;
	public static void updateWeightLoss(){
		calories = WeightLossActivity.caloriesToLose;
	}
}
