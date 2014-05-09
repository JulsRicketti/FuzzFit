package com.example.jfitnessfunctiontester;

import persistance.DatabaseAdapter;
import android.content.Context;
import android.database.Cursor;


public class User {

	DatabaseAdapter dbAdapter;
	
	String age;
	String weight;
	String height;
	String sex;
	String weightLossObjective;
	String runningObjective;
	String walkingObjective;
	String parameter;
	
	//gets his data from the database
	public User(Context context){
		dbAdapter = new DatabaseAdapter(context);
		dbAdapter.open();
		Cursor userData = dbAdapter.getAllUserRecords();
		userData.moveToFirst();
		
		while(!userData.isAfterLast()){
			age = userData.getString(userData.getColumnIndex(DatabaseAdapter.AGE));
			weight = userData.getString(userData.getColumnIndex(DatabaseAdapter.WEIGHT));
			height = userData.getString(userData.getColumnIndex(DatabaseAdapter.HEIGHT));
			sex = userData.getString(userData.getColumnIndex(DatabaseAdapter.SEX));
			weightLossObjective= userData.getString(userData.getColumnIndex(DatabaseAdapter.WEIGHT_LOSS_OBJECTIVE));
			runningObjective= userData.getString(userData.getColumnIndex(DatabaseAdapter.RUNNING_OBJECTIVE));
			walkingObjective = userData.getString(userData.getColumnIndex(DatabaseAdapter.WALKING_OBJECTIVE));
			parameter= userData.getString(userData.getColumnIndex(DatabaseAdapter.PARAMETER));
			userData.moveToNext();
		}
	}
	
	//when we are first setting his data
	public User(Context context, String age, String weight, String height, String sex, String weightLossObjective, 
			String runningObjective, String walkingObjective, String parameter){
		this.age =age;
		this.weight = weight;
		this.height = height;
		this.sex = sex;
		this.weightLossObjective = weightLossObjective;
		this.runningObjective = runningObjective;
		this.walkingObjective = walkingObjective;
		this.parameter = parameter;
		
		//we need to store him in the database
		dbAdapter = new DatabaseAdapter(context);
		dbAdapter.open();
		dbAdapter.insertUser(age, weight, height, sex, weightLossObjective, runningObjective, walkingObjective, parameter);
	}
	
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getWeightLossObjective() {
		return weightLossObjective;
	}
	public void setWeightLossObjective(String weightLossObjective) {
		this.weightLossObjective = weightLossObjective;
	}
	public String getRunningObjective() {
		return runningObjective;
	}
	public void setRunningObjective(String runningObjective) {
		this.runningObjective = runningObjective;
	}
	public String getWalkingObjective() {
		return walkingObjective;
	}
	public void setWalkingObjective(String walkingObjective) {
		this.walkingObjective = walkingObjective;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	
}
