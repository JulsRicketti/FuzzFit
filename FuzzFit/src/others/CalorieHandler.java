package others;

import android.content.Context;

public class CalorieHandler {

	Context context;
	
	User user;
	float calories;
	float met;
	
	
	public float getCalories() {
		return calories;
	}

	public void setCalories(float calories) {
		this.calories = calories;
	}

	public float getMet() {
		return met;
	}

	public void setMet(float met) {
		this.met = met;
	}

	public CalorieHandler(Context context){
		this.context = context;
		this.user = new User(context);
	}
	
	public void calculateCalories(float velocity, float time){
		met = defineMET(velocity);
		calories += Float.parseFloat(user.getWeight())*(time/60)*met; //time comes in minutes!
	}

	//(in km/h)
	float defineMET(float velocity){
		if(velocity<3.2)
			return (float) 2.0;
		if(velocity>=3.2 && velocity<4.5)
			return (float) 3.0;
		if(velocity>=4.5 && velocity<5.2)
			return (float) 3.5;
		if(velocity>=5.2 && velocity<6.4)
			return (float) 5.0;
		if(velocity>=6.4 && velocity<7.2)
			return (float) 6.0;
		if(velocity>=7.2 && velocity<8.1)
			return (float) 8.3;
		if(velocity>=8.1 && velocity<9.7)
			return (float) 9.8;
		if(velocity>=9.7 && velocity<11.3)
			return (float) 11.0;
		if(velocity>=11.3 && velocity<12.9)
			return (float) 11.8;
		if(velocity>=12.9 && velocity<14.5)
			return (float) 12.8;
		if(velocity>=14.5 && velocity<16.1)
			return (float) 14.5;
		if(velocity>=16.1 && velocity<17.8)
			return (float) 16.0;
		if(velocity>=17.8 && velocity<19.3)
			return (float) 19.0;
		if(velocity>=19.3 && velocity<20.9)
			return (float) 19.8;
		if(velocity>=20.9 && velocity<22.5)
			return (float) 23.0;
		if(velocity>=20.9)
			return (float) 23.0;
		
		return 0;
	}
	
	public float calculateActivityVelocity(float calories, float time){
		float met = (calories)/(time*Float.parseFloat(user.getWeight()));
		return defineVelocity(met);
	}
	
	//(in km/h)
	float defineVelocity(float met){
		if(met<=2.0)
			return (float) 3.2;
		if(met>2.0 && met<=3.0)
			return (float) 4.5;
		if(met>3.0 && met<=3.5)
			return (float) 5.2;
		if(met >3.5 && met<=5.0)
			return (float) 6.4;
		if(met>5.0 && met<=6.0)
			return (float) 7.2;
		if(met>6.0 && met<=8.3)
			return (float) 7.2;
		if(met>8.3 && met<=9.8)
			return (float) 8.1;
		if(met>9.8 && met<=11.0)
			return (float) 9.7;
		if(met>11.0 && met<=11.8)
			return (float)11.3;
		if(met>11.8 && met<=12.8)
			return (float) 12.9;
		if(met>12.8 && met<=14.5)
			return (float) 14.5;
		if(met>14.5 && met<=16.0)
			return (float) 16.1;
		if(met>16.0 && met<=19.0)
			return (float) 17.8;
		if(met>19.0 && met<=19.8)
			return (float) 19.3;
		if(met>19.8 && met<=23.0)
			return (float)20.9;
		if(met>23.0)
			return (float) 22.5;
		
		return 0;
	}
	
}
