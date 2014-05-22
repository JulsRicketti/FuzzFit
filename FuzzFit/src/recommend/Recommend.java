package recommend;


import android.content.Context;

public interface Recommend {

	
	String recommend(Context context);
//	void enterActivity(float time, float distance);
//	float getLastMonitorResult();
	float getLastRecommendation();
	
	void increaseRecommendation();
	void decreaseRecommendation();
	void maintainRecommendation();
	void updateRecommendation(float newRecommendation);
}
