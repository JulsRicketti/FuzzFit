package recommend;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Days;

import others.Mediator;

import android.content.Context;


//try to make it a singleton
public class WalkingRecommend implements Recommend {

	Context context;
	
	public float distanceInput; //this will come from the user
	
	static boolean completedWeeklyDistance = false;
	static int numberOfDays = 7; //this is the number of days we take into consideration
	
	static float weeklyMinDistance = 5500;
	static float dailyMinDistance = weeklyMinDistance/numberOfDays;
	
	static float currentDailyMinDistance;
	
	Mediator mediator;
	
	final float increasePercentage = (float) 0.1; //(10% as of now)
	float currentRecommendation; //the default is before any activities are done

	boolean isNotEmpty;
	public WalkingRecommend(Context context){
		this.context = context;
		mediator = new Mediator(context);
		isNotEmpty = mediator.setWalkingHistory();
		if(isNotEmpty)
			currentRecommendation = getLastRecommendation();
		else
			currentRecommendation = dailyMinDistance;
		
	}

	
	public String recommend(Context context){
		return Float.toString(currentRecommendation);
	}
	
	
	public float getLastRecommendation(){
		if(isNotEmpty)
			return Float.parseFloat(mediator.getRecommendationHistory().get(mediator.getRecommendationHistory().size()-1));
		else
			return dailyMinDistance;
	}

	@Override
	public void increaseRecommendation() {
		// TODO Auto-generated method stub
		currentRecommendation = currentRecommendation + (currentRecommendation*increasePercentage);
		//return Float.toString(currentRecommendation + (currentRecommendation*increasePercentage));
	}

	@Override
	public void decreaseRecommendation() {
		// TODO Auto-generated method stub
		currentRecommendation = currentRecommendation - (currentRecommendation*increasePercentage);
		if(currentRecommendation<dailyMinDistance)
			currentRecommendation = dailyMinDistance;
	}

	@Override
	public void maintainRecommendation() {
		// TODO Auto-generated method stub
		currentRecommendation = getLastRecommendation();
		
	}

	@Override
	public void updateRecommendation() {
		// TODO Auto-generated method stub
		
	}
}
