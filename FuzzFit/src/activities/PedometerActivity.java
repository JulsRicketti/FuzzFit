package activities;

import java.util.Timer;
import java.util.TimerTask;

import others.CalorieHandler;
import others.User;

import monitor.MonitorObserver;
import recommend.Recommend;
import recommend.RunningRecommend;
import recommend.WalkingRecommend;
import recommend.WeightLossRecommend;

import com.example.jfitnessfunctiontester.R;
import com.example.jfitnessfunctiontester.R.id;
import com.example.jfitnessfunctiontester.R.layout;
import com.example.jfitnessfunctiontester.R.menu;


import analyse.Analyse;
import analyse.RunningAnalyse;
import analyse.WalkingAnalyse;
import analyse.WeighLossAnalyse;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.os.Build;

public class PedometerActivity extends Activity implements SensorEventListener{
	
	static final int CALORIE_UPDATE_INTERVAL = 60000;
	CalorieHandler calories;
	
	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	final String walkerTitle = "Walk Training";
	final String runnerTitle = "Run Training";
	final String weightLossTitle = "Weight Loss Program";
	//How to find the stride?
	//source : http://walking.about.com/cs/pedometers/a/pedometerset.htm
	//height*strideLengh
	final float maleStrideLengthConstant = 0.415f;
	final float femaleStrideLengthConstant = 0.413f;
	
	float strideLength;
	float height = 1.64f;
	float distance=0;

	//variables for the sounds:
	MediaPlayer minuteSound;
	MediaPlayer goFasterSound;
	
	
	TextView sensitivityTextView; //display field of sensitivity 
	TextView stepsTextView; //display for steps
	
	Chronometer chronometer;
	long timeWhenStopped =0;
	
	Button resetButton;
	Button startPedometerButton;
	Button pausePedometerButton;
	Button finishPedometerButton;
	//sensor manager
	SensorManager sensorManager;
	float acceleration;
	
	//values to calculate number of steps:
	float previousY;
	float currentY;
	int numSteps;
	
	//Seek bar:
	SeekBar seekBar;
	int threshould;
	
	Context context = this;
	
	//Fitness related stuff:
	Recommend recommend;
	Analyse analyse;
	
	TextView recommendationTextView;
	
	int aprovado =100;
	private Handler handler = new Handler();
	 	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		
		minuteSound = MediaPlayer.create(this, R.raw.minute_beep);
		goFasterSound = MediaPlayer.create(this, R.raw.go_faster);
		
		//set screen title
		if(MainActivity.activityOption.equals(walkerOption))
			setTitle(walkerTitle+" Pedometer");
		if(MainActivity.activityOption.equals(runnerOption))
			setTitle(runnerTitle+" Pedometer");
		if(MainActivity.activityOption.equals(weightLossOption))
			setTitle(weightLossTitle+" Pedometer");

		
		calories = new CalorieHandler(context);
		calories.setCalories(0);
		
		context = this;
		User user = new User(this);

		
		if(user.getSex()=="Male")
			strideLength = Float.parseFloat(user.getHeight())*maleStrideLengthConstant;
		else
			strideLength = Float.parseFloat(user.getHeight())*femaleStrideLengthConstant;
		
		
		chronometer = (Chronometer) findViewById(R.id.chronometerChronometer);
		
		resetButton = (Button) findViewById(R.id.resetButton);
		startPedometerButton = (Button) findViewById(R.id.startPedometerButton);
		pausePedometerButton = (Button) findViewById(R.id.pausePedometerButton);
		finishPedometerButton = (Button) findViewById(R.id.finishPedometerButton);
		
		setButtons();
		
		sensitivityTextView = (TextView) findViewById(R.id.sensitivityTextView);
		stepsTextView = (TextView) findViewById(R.id.stepsTextView);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setProgress(10);
		seekBar.setOnSeekBarChangeListener(seekBarListener);
		threshould=10;
		sensitivityTextView.setText(String.valueOf(threshould));
		
		//initialize values:
		previousY =0;
		currentY =0;
		numSteps =0;
		
		acceleration = 0.00f;
		//setting stuff related to physical activies:
		recommendationTextView =(TextView) findViewById(R.id.pedometerRecommendationTextView);
		if(MainActivity.activityOption.equals(walkerOption)){
			recommend = new WalkingRecommend(this);
			recommendationTextView.setText(getString(R.string.recommendation_enter)+" "+recommend.recommend(this)+" "+getString(R.string.text_view_recommendation_distance_enter));
			//	recommendationTextView.setText("Recommendation: "+recommend.recommend(this)+" meters");			
		}
		if(MainActivity.activityOption.equals(runnerOption)){
			recommend = new RunningRecommend(this);
			recommendationTextView.setText(getString(R.string.recommendation_enter)+recommend.recommend(this)+getString(R.string.text_view_recommendation_time_enter));
			//recommendationTextView.setText("Recommendation: "+recommend.recommend(this)+" minutes");
		}
		if(MainActivity.activityOption.equals(weightLossOption)){
			//We need to estimate velocity and calories every minute or so
			recommendationTextView.setText(getString(R.string.recommendation_enter)+WeightLossActivity.activityDistance+" "+ getString(R.string.text_view_recommendation_distance_km_enter)+" "+(WeightLossActivity.activityTime)*60+getString(R.string.text_view_recommendation_time_enter));
			//recommendationTextView.setText("Recommendation: "+WeightLossActivity.activityDistance+ "km in "+(WeightLossActivity.activityTime)*60+" minutes");

		}
		
	}
	
//calls the calorie counter function
	private Runnable timedTask = new Runnable(){

		  @Override
		  public void run() {
			  calculateCalories();
			  handler.postDelayed(timedTask, CALORIE_UPDATE_INTERVAL);
			  minuteSound.start();
		  }
	 };

	float previousDistance =0;
	float currentDistanceInterval =0;
	void setCurrentDistance(){
		
		if(previousDistance ==0){
			currentDistanceInterval = distance;
			previousDistance = distance;
		}
		else{
			currentDistanceInterval = distance-previousDistance;
			previousDistance = distance;
		}
	}
	
	float kmHConstant = (float) 3.6;
	float calculateVelocity(){
		//v=d/tv (m/s)
		return (currentDistanceInterval/60)*kmHConstant; //by the end of the calculation, we convert it to km/h
	}

	void calculateCalories(){
		setCurrentDistance();
		//beep telling you to speed up a bit
		if(calculateVelocity()<1)
			goFasterSound.start();
		calories.calculateCalories(calculateVelocity(), 1); //here we need to give the time in minutes
	}
	 
	void setButtons(){
		finishPedometerButton.setEnabled(false);
		resetButton.setEnabled(false);
		pausePedometerButton.setEnabled(false);
		
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.start();
				resetSteps(v);
				
			}
		});
		
		startPedometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				enableAccelerometerListening();
				chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
				chronometer.start();
				startPedometerButton.setEnabled(false);
				pausePedometerButton.setEnabled(true);
				finishPedometerButton.setEnabled(true);
				resetButton.setEnabled(true);
				handler.post(timedTask); //start handler (to calculate calories & other stuff)
				
				//check to see if it's better to be put here
//				if(MainActivity.activityOption.equals(weightLossOption))
//					handler.post(timedTask); //to calculate the calories
				
			}
		});
		pausePedometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sensorManager.unregisterListener(sensorEventListener);
				timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
				chronometer.stop();
				handler.removeCallbacks(timedTask);
				startPedometerButton.setEnabled(true);
				pausePedometerButton.setEnabled(false);
				
			}
		});
		finishPedometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				   float time = (SystemClock.elapsedRealtime() - chronometer.getBase())/60000;
					sensorManager.unregisterListener(sensorEventListener);
					timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
					chronometer.stop();
					handler.removeCallbacks(timedTask);
				    
					resetButton.setEnabled(false);
					startPedometerButton.setEnabled(false);
					pausePedometerButton.setEnabled(false);
				
					
					if(MainActivity.activityOption.equals(walkerOption)){
						MonitorObserver.updateWalk(context); 
						analyse = new WalkingAnalyse(context);
						analyse.enterActivity(time, distance, calories.getCalories()); //this is what to do whenever inserting a new activity
						recommend = new WalkingRecommend(context);
						//recommendationTextView.setText("Next Recommendation: "+recommend.recommend(context)+" meters");
						recommendationTextView.setText(R.string.text_view_next_recommendation_enter+recommend.recommend(context)+" "+R.string.text_view_recommendation_distance_enter);
					}
					if(MainActivity.activityOption.equals(runnerOption)){
						MonitorObserver.updateRun(context);
						analyse = new RunningAnalyse(context);
						analyse.enterActivity(time, distance, calories.getCalories());
						recommend = new RunningRecommend(context);
						//recommendationTextView.setText("Next Recommendation: "+recommend.recommend(context)+" minutes");
						recommendationTextView.setText(R.string.text_view_next_recommendation_enter+recommend.recommend(context)+" "+R.string.text_view_recommendation_time_enter);
					}	
					if(MainActivity.activityOption.equals(weightLossOption)){
						MonitorObserver.updateWeightLoss();
						analyse = new WeighLossAnalyse(context);
						analyse.enterActivity(time, distance, calories.getCalories());
						recommend = new WeightLossRecommend(context);
						//recommendationTextView.setText("Next Recommendation: "+recommend.recommend(context)+" calories");
						recommendationTextView.setText(R.string.text_view_next_recommendation_enter+recommend.recommend(context)+R.string.text_view_recommendation_calories_enter);
					}
			}
		});

	}
	
	private void enableAccelerometerListening(){
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	private SensorEventListener sensorEventListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			currentY = y;
			
			if(Math.abs(currentY-previousY) > threshould){
				numSteps++;
				distance+=strideLength/100; //we convert that to meters!
				stepsTextView.setText(getString(R.string.estimated_steps)+" "+String.valueOf(numSteps)+"\n"+ getString(R.string.estimated_distance) +distance+"\n"+getString(R.string.estimated_calories)+calories.getCalories());
				
			}
			
			previousY = y;

			
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void resetSteps(View v){
		numSteps =0;
		stepsTextView.setText(String.valueOf(numSteps));
	}
	
	private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			threshould = seekBar.getProgress();
			sensitivityTextView.setText(String.valueOf(threshould));
			
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
}
