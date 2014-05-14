package recommend;

import others.Mediator;
import android.content.Context;

public class WeightLossRecommend implements Recommend{

	final float increaseConstant = 50;
	final float minimumCaloriesRecommendation = 100; 
	
	Context context;
	Mediator mediator;
	
	boolean isNotEmpty;
	float currentCaloriesRecommendation;
	
	public WeightLossRecommend(Context context){
		this.context = context;
		mediator = new Mediator(context);
		isNotEmpty = mediator.setWeightLossHistory();
		if(isNotEmpty)
			currentCaloriesRecommendation= getLastRecommendation();
		else
			currentCaloriesRecommendation= minimumCaloriesRecommendation;
	}
	
	@Override
	public String recommend(Context context) {
		return Float.toString(currentCaloriesRecommendation);
	}

	@Override
	public float getLastRecommendation() {
		if(isNotEmpty)
			return Float.parseFloat(mediator.getRecommendationHistory().get(mediator.getRecommendationHistory().size()-1));
		else
			return minimumCaloriesRecommendation;
	}

	@Override
	public void increaseRecommendation() {
		currentCaloriesRecommendation += increaseConstant;
		
	}

	@Override
	public void decreaseRecommendation() {
		currentCaloriesRecommendation -= increaseConstant;
		
	}

	@Override
	public void maintainRecommendation() {
		currentCaloriesRecommendation = getLastRecommendation();
		
	}

	@Override
	public void updateRecommendation() {
		// TODO Auto-generated method stub
		
	}


}
