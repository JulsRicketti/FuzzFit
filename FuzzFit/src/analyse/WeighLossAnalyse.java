package analyse;

import org.joda.time.DateTime;
import org.joda.time.Days;

import recommend.Recommend;
import recommend.WalkingRecommend;
import recommend.WeightLossRecommend;
import android.content.Context;

import com.example.jfitnessfunctiontester.Mediator;
import com.example.jfitnessfunctiontester.User;

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
	
	void calculateCalories(float time, float velocity){
		float met = defineMET(velocity); 
		calories = Float.parseFloat(user.getWeight())*(time/60)*met;
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
