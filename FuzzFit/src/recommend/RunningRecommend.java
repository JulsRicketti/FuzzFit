package recommend;

import com.example.jfitnessfunctiontester.Mediator;

import android.content.Context;

public class RunningRecommend implements Recommend {

	Context context;
	Mediator mediator;
	
	static float minimumTimeRecommendation = 10;
	
	float currentTimeRecommendation = 10; //the recommendation is in minutes 10 is the starting
	
	boolean isNotEmpty;
	
	public RunningRecommend(Context context){
		this.context = context;
		mediator = new Mediator(context);
		isNotEmpty = mediator.setRunningHistory();
		if(isNotEmpty)
			currentTimeRecommendation= getLastRecommendation();
		else
			currentTimeRecommendation= minimumTimeRecommendation;
	}
	
	@Override
	public String recommend(Context context) {
		return Float.toString(currentTimeRecommendation);
	}


	@Override
	public float getLastRecommendation() {
		if(isNotEmpty)
			return Float.parseFloat(mediator.getRecommendationHistory().get(mediator.getRecommendationHistory().size()-1));
		else
			return minimumTimeRecommendation;
	}

	@Override
	public void increaseRecommendation() {
		// TODO Auto-generated method stub
		if(currentTimeRecommendation==minimumTimeRecommendation){
			currentTimeRecommendation = 12;
			return;
		}
		if(currentTimeRecommendation ==12){
			currentTimeRecommendation = 15;
			return;
		}
		if(currentTimeRecommendation>=14){
			currentTimeRecommendation = currentTimeRecommendation +5;
			return;
		}
		//check out later what to do if we reach 1 hour!!
	}

	@Override
	public void decreaseRecommendation() {
		if(currentTimeRecommendation==minimumTimeRecommendation){ //we cannot decrease from here!!
			currentTimeRecommendation = minimumTimeRecommendation;
			return;
		}
		if(currentTimeRecommendation == 12){
			currentTimeRecommendation = minimumTimeRecommendation;
			return;
		}
		if(currentTimeRecommendation == 15){
			currentTimeRecommendation = 12;
			return;
		}
		if(currentTimeRecommendation>15){
			currentTimeRecommendation = getLastRecommendation() -5;
			return;
		}
	}

	@Override
	public void maintainRecommendation() {
		currentTimeRecommendation = getLastRecommendation();
		
	}

	@Override
	public void updateRecommendation() {
		// TODO Auto-generated method stub
		
	}

}
