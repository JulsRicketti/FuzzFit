package history;

import java.util.ArrayList;

public class ActivityHistory {

	public ArrayList <String> recommendationDate;
	public ArrayList <String> recommendation;
	public ArrayList <String> activityDate;
	public ArrayList <String> activityDistance;
	public ArrayList <String> activityTime;
	public ArrayList <String> monitor;
	public ArrayList <String> calories;
	
	public History history;
	
	public ActivityHistory(){
		recommendation = new ArrayList<String>();
		recommendationDate = new ArrayList<String>();
		activityDate = new ArrayList<String>();
		activityDistance = new ArrayList<String>();
		activityTime = new ArrayList<String>();
		monitor = new ArrayList<String>();
		calories = new ArrayList<String>();
	}
	
	public ArrayList<String> getRecommendationDate() {
		return recommendationDate;
	}
	public void setRecommendationDate(ArrayList<String> recommendationDate) {
		this.recommendationDate = recommendationDate;
	}
	public ArrayList<String> getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(ArrayList<String> recommendation) {
		this.recommendation = recommendation;
	}
	public ArrayList<String> getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(ArrayList<String> activityDate) {
		this.activityDate = activityDate;
	}
	
	public ArrayList<String> getActivityDistance() {
		return activityDistance;
	}

	public void setActivityDistance(ArrayList<String> activityDistance) {
		this.activityDistance = activityDistance;
	}

	public ArrayList<String> getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(ArrayList<String> activityTime) {
		this.activityTime = activityTime;
	}

	public ArrayList<String> getMonitor() {
		return monitor;
	}
	public void setMonitor(ArrayList<String> monitor) {
		this.monitor = monitor;
	}

	public ArrayList<String> getCalories() {
		return calories;
	}

	public void setCalories(ArrayList<String> calories) {
		this.calories = calories;
	}

	

}
