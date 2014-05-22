package activities;

import others.CalorieHandler;
import monitor.MonitorObserver;
import recommend.Recommend;
import recommend.RunningRecommend;
import recommend.WalkingRecommend;
import recommend.WeightLossRecommend;

import com.example.jfitnessfunctiontester.R;
import com.example.jfitnessfunctiontester.R.layout;
import com.example.jfitnessfunctiontester.R.menu;

import analyse.Analyse;
import analyse.RunningAnalyse;
import analyse.WalkingAnalyse;
import analyse.WeighLossAnalyse;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnterActivityActivity extends Activity {

	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	final String walkerTitle = "Walk Training";
	final String runnerTitle = "Run Training";
	final String weightLossTitle = "Weight Loss Program";
	
	TextView recommendationTextView;
	TextView changeRecommendationtextView;
	
	EditText distanceEditText;
	EditText timeEditText;
	String distanceString="";
	String timeString="";
	
	EditText changeRecommendationEditText;
	String changeRecommendationString="";
	
	Button enterButton;
	Button pedometerButton;
	Button changeRecommendationButton;
	
	Recommend recommend;
	Analyse analyse;
	
	CalorieHandler calorieHandler;
	
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter);

		
		//set screen title
		if(MainActivity.activityOption.equals(walkerOption))
			setTitle(walkerTitle);
		if(MainActivity.activityOption.equals(runnerOption))
			setTitle(runnerTitle);
		if(MainActivity.activityOption.equals(weightLossOption))
			setTitle(weightLossTitle);
		
		context = this;
		
		recommendationTextView = (TextView) findViewById(R.id.recommendationTextView);
		changeRecommendationtextView = (TextView) findViewById(R.id.changeRecommendationtextView);
		
		distanceEditText = (EditText) findViewById(R.id.distanceEditText);
		distanceEditText.addTextChangedListener(distanceTextWatcher);
		timeEditText = (EditText) findViewById(R.id.timeEditText);
		timeEditText.addTextChangedListener(timeTextWatcher);
		
		changeRecommendationEditText = (EditText) findViewById(R.id.changeRecommendationEditText);
		changeRecommendationEditText.addTextChangedListener(changeRecommendationTextWatcher);
		
		calorieHandler = new CalorieHandler(context);
		
		enterButton = (Button) findViewById(R.id.activityEnterButton);
		pedometerButton = (Button) findViewById(R.id.pedometerButton);
		changeRecommendationButton =(Button) findViewById(R.id.changeRecommendationButton);
		setButtons();
		
		//we need to set the kind of activity we are doing here:
		if(MainActivity.activityOption.equals(walkerOption)){
			recommend = new WalkingRecommend(this);
			recommendationTextView.setText(getString(R.string.recommendation_enter)+" "+recommend.recommend(this)+" "+getString(R.string.text_view_recommendation_distance_enter));
		}
		if(MainActivity.activityOption.equals(runnerOption)){
			recommend = new RunningRecommend(this);
			recommendationTextView.setText(getString(R.string.recommendation_enter)+recommend.recommend(this)+getString(R.string.text_view_recommendation_time_enter));
		}
		if(MainActivity.activityOption.equals(weightLossOption)){
			//we need to make the adjustment options invisible
			changeRecommendationtextView.setVisibility(View.GONE);
			changeRecommendationButton.setVisibility(View.GONE);
			changeRecommendationEditText.setVisibility(View.GONE);
			
			recommendationTextView.setText(getString(R.string.recommendation_enter)+WeightLossActivity.activityDistance+" "+ getString(R.string.text_view_recommendation_distance_km_enter)+" "+(WeightLossActivity.activityTime)*60+getString(R.string.text_view_recommendation_time_enter));
		}
	}

	void setButtons(){
		changeRecommendationButton.setEnabled(false);//becomes true only if the change text field is not empty
		enterButton.setEnabled(false);
		
		enterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				float timeHours = Float.parseFloat(timeString)/60; //time converted into hours
				float distanceKM = Float.parseFloat(distanceString)/1000;
				float velocityKM = distanceKM/timeHours;
				calorieHandler.calculateCalories(velocityKM, timeHours);
				if(MainActivity.activityOption.equals(walkerOption)){
					MonitorObserver.updateWalk(context); 
					analyse = new WalkingAnalyse(context);
					analyse.enterActivity(Float.parseFloat(timeString), Float.parseFloat(distanceString), calorieHandler.getCalories()); //this is what to do whenever inserting a new activity
					recommend = new WalkingRecommend(context);
					recommendationTextView.setText(R.string.text_view_next_recommendation_enter+recommend.recommend(context)+" "+R.string.text_view_recommendation_distance_enter);
				
				}
				if(MainActivity.activityOption.equals(runnerOption)){
					MonitorObserver.updateRun(context);
					analyse = new RunningAnalyse(context);
					analyse.enterActivity(Float.parseFloat(timeString), Float.parseFloat(distanceString), calorieHandler.getCalories());
					recommend = new RunningRecommend(context);
					recommendationTextView.setText(R.string.text_view_next_recommendation_enter+recommend.recommend(context)+" "+R.string.text_view_recommendation_time_enter);
					
				}
				if(MainActivity.activityOption.equals(weightLossOption)){

					
					
					MonitorObserver.updateWeightLoss();
					analyse = new WeighLossAnalyse(context);
					analyse.enterActivity(Float.parseFloat(timeString), Float.parseFloat(distanceString), calorieHandler.getCalories());
					recommend = new WeightLossRecommend(context);
					recommendationTextView.setText(R.string.text_view_next_recommendation_enter+recommend.recommend(context)+R.string.text_view_recommendation_calories_enter);
				}
			}
		});
		
		pedometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i  = new Intent(EnterActivityActivity.this,PedometerActivity.class);
				startActivity(i);
				
			}
		});
		
		changeRecommendationButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recommend.updateRecommendation(Float.parseFloat(changeRecommendationString));
				if(MainActivity.activityOption.equals(walkerOption)){
					recommendationTextView.setText(R.string.recommendation_enter+recommend.recommend(context)+R.string.text_view_recommendation_distance_enter);			
				}
				if(MainActivity.activityOption.equals(runnerOption)){
					recommendationTextView.setText(R.string.recommendation_enter+recommend.recommend(context)+R.string.text_view_recommendation_time_enter);
				}
			}
		});
	}
	
	TextWatcher changeRecommendationTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			changeRecommendationString = s.toString();
			if(changeRecommendationString.equals("")){
				changeRecommendationButton.setEnabled(false);
			}
			else{
				changeRecommendationButton.setEnabled(true);
			}
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	TextWatcher distanceTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			distanceString = s.toString();

			if(!distanceString.equals(""))
				enterButton.setEnabled(true);
			if(timeString.equals("") || distanceString.equals("")){
				enterButton.setEnabled(false);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	TextWatcher timeTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			timeString = s.toString();

			if(!timeString.equals(""))
				enterButton.setEnabled(true);
			if(distanceString.equals("")||timeString.equals("") ){
				enterButton.setEnabled(false);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.walking, menu);
		return true;
	}

}
