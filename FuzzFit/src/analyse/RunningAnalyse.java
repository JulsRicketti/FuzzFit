package analyse;

import org.joda.time.DateTime;
import org.joda.time.Days;

import others.Mediator;

import recommend.Recommend;
import recommend.RunningRecommend;
import recommend.WalkingRecommend;
import android.content.Context;


public class RunningAnalyse implements Analyse{

	//Our objective is to get to this!
	//Trabalho de Conclusão de Curso II - Aprovado
	//Situação: Aprovado
	
	private static float improvementPercentage = 90;
	private static float averagePercentage = 60;
	
	Context context;
	Mediator mediator;
	Recommend recommend;
	
	boolean isFirstActivity;
	float lastRecommendation;
	float lastMonitor;
	float lastActivity;
	
	public RunningAnalyse(Context context){
		this.context = context;
		mediator = new Mediator(context);
		recommend = new RunningRecommend(context);
		isFirstActivity = !(mediator.setRunningHistory());
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

	float time, distance, velocity;
	float monitorResult;
	@Override
	public void enterActivity(float time, float distance, float calories) {
		this.time =time;
		this.distance =distance;
		
		String date = getCurrentDate().toString().substring(0, 10);
		try{
			monitorResult = Float.parseFloat(mediator.monitorRunner(time, distance, 0));			 
			analyse();
			mediator.buildRunnerEntry(recommend.recommend(context), date, Float.toString(distance), Float.toString(time), Float.toString(calories));
		}
		catch(ArrayIndexOutOfBoundsException e){
			monitorResult = Float.parseFloat(mediator.monitorRunner(time, distance, 0));
			analyse();
			mediator.buildRunnerEntry(recommend.recommend(context), date, Float.toString(distance), Float.toString(time), Float.toString(calories));
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

	@Override
	public void enterCalories(float calories) {
		// TODO Auto-generated method stub
		
	}

}
