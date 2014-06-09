package activities;

import com.fuzzfit.R;

import monitor.MonitorObserver;
import others.CalorieHandler;
import others.Mediator;
import recommend.Recommend;
import recommend.WeightLossRecommend;
import analyse.Analyse;
import analyse.WeighLossAnalyse;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WeightLossActivity extends Activity {

	Context context = this;
	
	Analyse analyse;
	Recommend recommend;
	
	Button wlRecommendationButton;
	Button wlSwitchExerciseButton;
	Button wlEnterActivityButton;
	
	TextView caloriesRecommendationTextView;
	TextView wlExerciseOptionTextView;
	
	EditText adjustExerciseTimeEditText;
	String adjustExerciseTime="";
	EditText wlCalorieConsumptionEditText;
	String calorieConsumption="";
	
	Mediator mediator; 
	CalorieHandler caloriesHandler;
	
	
	public static float caloriesToLose, activityVelocity, activityTime, activityDistance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight_loss);
		
		setTitle(getString(R.string.pedometer_weight_loss_title));
		
		caloriesRecommendationTextView = (TextView) findViewById(R.id.caloriesRecommendationTextView);
		wlExerciseOptionTextView = (TextView) findViewById(R.id.wlExerciseOptionTextView);
		
		wlCalorieConsumptionEditText = (EditText) findViewById(R.id.wlCalorieConsumptionEditText);
		wlCalorieConsumptionEditText.addTextChangedListener(calorieConsumptionTextWatcher);
		
		adjustExerciseTimeEditText = (EditText) findViewById(R.id.adjustExerciseTimeEditText);
		adjustExerciseTimeEditText.addTextChangedListener(adjustExerciseTimeTextWatcher);
		
		wlRecommendationButton = (Button) findViewById(R.id.wlRecommendationButton);
		wlSwitchExerciseButton = (Button) findViewById(R.id.wlSwitchExerciseButton);
		wlEnterActivityButton = (Button) findViewById(R.id.wlEnterActivityButton);
		setButtons();
		
		analyse = new WeighLossAnalyse(context);
		recommend = new WeightLossRecommend(context);
		caloriesHandler = new CalorieHandler(context);
		
		wlEnterActivityButton.setEnabled(false);
		wlSwitchExerciseButton.setEnabled(false);
		
	}
	
	TextWatcher adjustExerciseTimeTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			adjustExerciseTime = s.toString();
			if(adjustExerciseTime.equals(""))
				wlSwitchExerciseButton.setEnabled(false);
			else
				wlSwitchExerciseButton.setEnabled(true);
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
	
	TextWatcher calorieConsumptionTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			calorieConsumption = s.toString();
			
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
	
	int variationNumber =0; //used to get different variations of the amount of calories
	void setButtons(){
		wlSwitchExerciseButton.setEnabled(false);
		adjustExerciseTimeEditText.setEnabled(false);
		
		wlRecommendationButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				wlEnterActivityButton.setEnabled(true);
				float calorieConsumptionValue=0;
				if(!calorieConsumption.equals("")){
					calorieConsumptionValue = Float.parseFloat(calorieConsumption);	
					if(calorieConsumptionValue<1500){
						Toast.makeText(getApplicationContext(), getString(R.string.low_calorie_message), Toast.LENGTH_LONG).show();
						caloriesRecommendationTextView.setText(getString(R.string.consume_more_calories_message));
						return;
					}
					if(calorieConsumptionValue>2500){
						Toast.makeText(getApplicationContext(), R.string.high_calorie_message, Toast.LENGTH_LONG).show();
						
					}
				}
				else{ //if the user doesnt specify, we assume we are working with 2000 calories daily 
					Toast.makeText(getApplicationContext(), R.string.default_calorie_message, Toast.LENGTH_LONG).show();
					calorieConsumptionValue = 2000;
				}
				if(calorieConsumptionValue>1500){
//					if(recommend.getLastRecommendation()<500)
//						recommend.increaseRecommendation();
					caloriesToLose = (Float.parseFloat(recommend.recommend(context)));
				}
				else{
					caloriesToLose = calorieConsumptionValue-1000;
				}
				caloriesRecommendationTextView.setText(caloriesToLose+" calories");
				//we need to give them the kind of exercise we can do to get those calories off!
				activityTime = 1; //(default is 1 hour)
				activityVelocity = caloriesHandler.calculateActivityVelocity(caloriesToLose, activityTime); //comes in km/h
				activityDistance = activityVelocity*(activityTime);
				wlExerciseOptionTextView.setText(getString(R.string.weight_loss_distance)+activityDistance+getString(R.string.weight_loss_distance_unit)+"\n"+getString(R.string.weight_loss_time)+(activityTime*60)+getString(R.string.weight_loss_time_unit));
				adjustExerciseTimeEditText.setEnabled(true);
				MonitorObserver.updateWeightLoss(); //update the observer so we know the values we are monitoring

			}
		});
		
		
		wlSwitchExerciseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activityTime = (Float.parseFloat(adjustExerciseTime))/60; 
				activityVelocity = caloriesHandler.calculateActivityVelocity(caloriesToLose, activityTime); //comes in km/h
				activityDistance = activityVelocity*(activityTime);
				String activityDistanceString = String.format("%.2f", activityDistance);
				activityDistance = Float.parseFloat(activityDistanceString); //just did this to make it a more reasonable number
				wlExerciseOptionTextView.setText(getString(R.string.weight_loss_distance)+activityDistanceString+getString(R.string.weight_loss_distance_unit)+"\n"+getString(R.string.weight_loss_time)+adjustExerciseTime+getString(R.string.weight_loss_time_unit));

			}
		});
		
		wlEnterActivityButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i  = new Intent(WeightLossActivity.this,EnterActivityActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weight_loss, menu);
		return true;
	}

}
