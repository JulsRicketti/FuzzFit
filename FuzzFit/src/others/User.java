package others;

import com.example.jfitnessfunctiontester.R;

import persistance.DatabaseAdapter;
import android.content.Context;
import android.database.Cursor;


public class User {

	DatabaseAdapter dbAdapter;
	
	String age;
	String weight;
	String height;
	String sex;

	
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
			userData.moveToNext();
		}
	}
	
	//when we are first setting his data
	public User(Context context, String age, String weight, String height, String sex){
		this.age =age;
		this.weight = weight;
		this.height = height;
		this.sex = sex;
		
		//we need to store him in the database
		dbAdapter = new DatabaseAdapter(context);
		dbAdapter.open();
		dbAdapter.insertUser(age, weight, height, sex);
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
	
	
	public float getAverageWalkingSpeed(){ //in km/h
		int age = Integer.parseInt(this.age);
		if(this.sex.equals("Male")){
			if(age<20)
				return (float)5.0148;
			if(age<30)
				return (float)5.2488;
			if(age<40)
				return (float)5.2632;
			if(age<50)
				return (float)5.0148;
			if(age<60)
				return (float)4.8924;
			if(age>=60)
				return (float)4.788;
		}
		else{
			if(age<20)
				return (float)5.0652;
			if(age<30)
				return (float)5.094;
			if(age<40)
				return (float)5.0076;
			if(age<50)
				return (float)5.022;
			if(age<60)
				return (float)4.6656;
			if(age>=60)
				return (float)4.5792;
		}
			
		return 0;
	}
	
	public float getAverageRunningSpeed(){ //(in m/s)
		//we also need to find the necessary distance.
		if(this.sex.equals("Male"))
			return (float) 3.8;
		else
			return (float) 2.9;
	}
}
