package others;

import history.ActivityHistory;
import history.History;

import java.util.ArrayList;

import monitor.Monitor;
import monitor.RunningMonitor;
import monitor.WalkingMonitor;
import monitor.WeightLossMonitor;

import persistance.DatabaseAdapter;

import recommend.Recommend;
import recommend.WalkingRecommend;
import android.app.Activity;
import android.content.Context;

public class Mediator {

	Context context;
	
	ActivityHistory walker;
	ActivityHistory runner;
	ActivityHistory weightLoss;
	
	public ArrayList <String> recommendationHistory= new ArrayList<String>();
	public ArrayList <String> activityDateHistory= new ArrayList<String>();
	public ArrayList <String> activityDistanceHistory= new ArrayList<String>();
	public ArrayList <String> activityTimeHistory= new ArrayList<String>();
	public ArrayList <String> monitorHistory= new ArrayList<String>();
	public ArrayList <String> calorieHistory = new ArrayList<String>(); 
	
	History history;
	Recommend recommendation;
	Monitor monitor;
	//Analyse analyses;
	
	public Mediator(Context context){
		this.context = context;
		this.history = new History(context);
		//this.recommendation = new WalkingRecommend();
		//this.analyses = new WalkingAnalyse();
	//	this.monitor = new WalkingMonitor();

	}
	

	//get all history functions
	 public boolean setWalkingHistory(){
		if(!history.isWalkerEmpty()){
			walker = history.getWalkerHistory();
			
			recommendationHistory = history.getWalkerHistory().recommendation;
			activityDistanceHistory = history.getWalkerHistory().activityDistance;
			activityTimeHistory = history.getWalkerHistory().activityTime;
			activityDateHistory = history.getWalkerHistory().activityDate;
			monitorHistory = history.getWalkerHistory().monitor;
			calorieHistory = history.getWalkerHistory().calories;
			return true; //it will return true when not empty
		}
			return false; //in case its empty, it will return false
	}
	
	 public boolean setRunningHistory(){
		 if(!history.isRunnerEmpty()){
			 runner = history.getRunnerHistory();
			 
			 recommendationHistory = history.getRunnerHistory().recommendation;
			 activityDistanceHistory = history.getRunnerHistory().activityDistance;
			 activityTimeHistory= history.getRunnerHistory().activityTime;
			 activityDateHistory = history.getRunnerHistory().activityDate;
			 monitorHistory = history.getRunnerHistory().monitor;
			 calorieHistory = history.getRunnerHistory().calories;
			 return true;
		 }
		 return false;
	 }
	
	 public boolean  setWeightLossHistory(){
		 if(!history.isWeightLossEmpty()){
			 weightLoss = history.getWeightLossHistory();
			 
			 recommendationHistory = history.getHistory("WeightLossHistoryTable").recommendation;
			 activityDistanceHistory = history.getHistory("WeightLossHistoryTable").activityDistance;
			 activityTimeHistory = history.getHistory("WeightLossHistoryTable").activityTime;
			 activityDateHistory = history.getHistory("WeightLossHistoryTable").activityDate;
			 monitorHistory = history.getHistory("WeightLossHistoryTable").monitor;
			 calorieHistory = history.getHistory("WeightLossHistoryTable").calories;
			 return true;
		 }
		 return false;
	}


	public ArrayList<String> getRecommendationHistory() {
		return recommendationHistory;
	}


	public ArrayList<String> getActivityDateHistory() {
		return activityDateHistory;
	}


	public ArrayList<String> getActivityDistanceHistory() {
		return activityDistanceHistory;
	}

	public ArrayList<String> getActivityTimeHistory() {
		return activityTimeHistory;
	}

	
	public String getLastActivityCalories(){
		return calorieHistory.get(calorieHistory.size()-1);
	}
	
	public String getLastActivityDistance(){
		return activityDistanceHistory.get(activityDistanceHistory.size()-1);
	}

	public String getLastActivityTime(){
		return activityTimeHistory.get(activityTimeHistory.size()-1);
	}

	
	public ArrayList<String> getMonitorHistory() {
		return monitorHistory;
	}
	
	//functions that verify is the database is empty
	
	public boolean isWalkerEmpty(){
		return history.isWalkerEmpty();
	}
	
	public void updateWalkerEntry(String recommendation, String activityDate, String activityDistance, String activityTime, String calories){
		float averageVelocity = (Float.parseFloat(activityDistance))/(Float.parseFloat(activityTime)/60);
		history.updateDatabase(DatabaseAdapter.WALKER_HISTORY_TABLE, recommendation, activityDate, activityDistance, activityTime, Float.toString(averageVelocity), getMonitorResult(), calories);
	}
	
	//For the next two ones remembers to change the situation with the velocity
	public void updateWeightLossEntry(String recommendation, String activityDate, String activityDistance, String activityTime, String calories){
		float averageVelocity = (Float.parseFloat(activityDistance))/(Float.parseFloat(activityTime)/60);
		history.updateDatabase(DatabaseAdapter.WALKER_HISTORY_TABLE, recommendation, activityDate, activityDistance, activityTime, Float.toString(averageVelocity), getMonitorResult(), calories);
	}
	
	public void updateRunnerEntry(String recommendation, String activityDate, String activityDistance, String activityTime, String calories){
		float averageVelocity = (Float.parseFloat(activityDistance))/(Float.parseFloat(activityTime)/60);
		history.updateDatabase(DatabaseAdapter.WALKER_HISTORY_TABLE, recommendation, activityDate, activityDistance, activityTime, Float.toString(averageVelocity), getMonitorResult(), calories);
	}
	
	//this is what will be used whenever it goes through the recommend class
	public void buildWalkerEntry(String recommendation, String activityDate, String activityDistance, String activityTime, String calories){
		float averageVelocity = (Float.parseFloat(activityDistance))/(Float.parseFloat(activityTime)/60);
		history.insert(DatabaseAdapter.WALKER_HISTORY_TABLE, recommendation, activityDate, activityDistance, activityTime,Float.toString(averageVelocity), getMonitorResult(), calories);
	}
	
	//For the next two ones remembers to change the situation with the velocity	
	public void buildWeightLossEntry(String recommendation, String activityDate, String activityDistance, String activityTime, String calories){
		float averageVelocity = (Float.parseFloat(activityDistance))/(Float.parseFloat(activityTime)/60);
		history.insert(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, recommendation, activityDate, activityDistance, activityTime,Float.toString(averageVelocity), getMonitorResult(), calories);
	}
	
	public void buildRunnerEntry(String recommendation, String activityDate, String activityDistance, String activityTime, String calories){
		float averageVelocity = (Float.parseFloat(activityDistance))/(Float.parseFloat(activityTime)/60);
		history.insert(DatabaseAdapter.RUNNER_HISTORY_TABLE, recommendation, activityDate, activityDistance, activityTime,Float.toString(averageVelocity), getMonitorResult(), calories);
	}
	
	
	public String getMonitorResult(){
		return Float.toString(this.monitor.getResult());
	}
	public String monitorWalker(float time, float distance){
		monitor = new WalkingMonitor(time, distance); //up to here on try number 2, everything is fine
		return Float.toString(monitor.getResult());
	}
	
	public String monitorWeightLoss(float calories){
		monitor = new WeightLossMonitor(calories);
		return Float.toString(monitor.getResult());
	}
	
	public String monitorRunner(float time, float distance, float velocity){
		monitor = new RunningMonitor(time, distance);
		return Float.toString(monitor.getResult());
	}
}
