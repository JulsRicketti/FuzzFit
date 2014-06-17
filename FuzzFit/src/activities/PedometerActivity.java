package activities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.fuzzfit.R;

import others.CalorieHandler;
import others.User;

import monitor.MonitorObserver;
import recommend.Recommend;
import recommend.RunningRecommend;
import recommend.WalkingRecommend;
import recommend.WeightLossRecommend;


import analyse.Analyse;
import analyse.RunningAnalyse;
import analyse.WalkingAnalyse;
import analyse.WeighLossAnalyse;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.os.Build;

public class PedometerActivity extends Activity implements SensorEventListener{
	
	static final int CALORIE_UPDATE_INTERVAL = 10000; //testing with 1 seconds
	static final int MINUTE_INTERVAL = 60000; //might use it for the sounds
	CalorieHandler calories;
	
	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
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
	MediaPlayer timerEndedSound; //(for the end of the timer)
	
	
	TextView sensitivityTextView; //display field of sensitivity 
	TextView stepsTextView; //display for steps
	TextView  countDownTextView; //the count down timer's text view
	
	Chronometer chronometer;
	long timeWhenStopped =0;
	
	Button resetButton;
	Button startPedometerButton;
	Button pausePedometerButton;
	Button finishPedometerButton;
	//sensor manager
	SensorManager sensorManager;
	float acceleration;
	
	CheckBox minuteSoundCheckBox;
	CheckBox fasterSoundCheckBox;
	
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
	private Handler soundHandler = new Handler();
	 	
	CounterClass timer; //the runner's timer
	
	float timeTimerAux=0; //to remind them how much time it went whenever the timer ended.
	
	User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		
		minuteSound = MediaPlayer.create(this, R.raw.minute_beep);
		goFasterSound = MediaPlayer.create(this, R.raw.go_faster);
		timerEndedSound = MediaPlayer.create(this, R.raw.applause);
		
		//set screen title
		if(MainActivity.activityOption.equals(walkerOption))
			setTitle(getString(R.string.pedometer_walker_title));
		if(MainActivity.activityOption.equals(runnerOption))
			setTitle(getString(R.string.pedometer_runner_title));
		if(MainActivity.activityOption.equals(weightLossOption))
			setTitle(getString(R.string.pedometer_weight_loss_title));

		
		calories = new CalorieHandler(context);
		calories.setCalories(0);
		
		context = this;
		user = new User(this);

		
		if(user.getSex().equals("Male"))
			strideLength = Float.parseFloat(user.getHeight())*maleStrideLengthConstant;
		else
			strideLength = Float.parseFloat(user.getHeight())*femaleStrideLengthConstant;
		
		
		chronometer = (Chronometer) findViewById(R.id.chronometerChronometer);
		
		resetButton = (Button) findViewById(R.id.resetButton);
		startPedometerButton = (Button) findViewById(R.id.startPedometerButton);
		pausePedometerButton = (Button) findViewById(R.id.pausePedometerButton);
		finishPedometerButton = (Button) findViewById(R.id.finishPedometerButton);
		
		setButtons();
		
		minuteSoundCheckBox = (CheckBox) findViewById(R.id.minuteSoundCheckBox);
		minuteSoundCheckBox.setChecked(true);
		fasterSoundCheckBox = (CheckBox) findViewById(R.id.fasterSoundCheckBox);
		fasterSoundCheckBox.setChecked(true);
		
		sensitivityTextView = (TextView) findViewById(R.id.sensitivityTextView);
		stepsTextView = (TextView) findViewById(R.id.stepsTextView);
		countDownTextView = (TextView) findViewById(R.id.countDownTextView);
		
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
			float recommendationAux = Math.round(Float.parseFloat(recommend.recommend(context)));
			recommendationTextView.setText(getString(R.string.recommendation_enter)+" "+recommendationAux+" "+getString(R.string.text_view_recommendation_distance_enter));
			countDownTextView.setVisibility(View.GONE); //the timer is only visible in the runner method			
		}
		if(MainActivity.activityOption.equals(runnerOption)){
			recommend = new RunningRecommend(this);
			MonitorObserver.updateRun(context); //Test!
			recommendationTextView.setText(getString(R.string.recommendation_enter)+" "+MonitorObserver.currentRunningDistance+" "+getString(R.string.text_view_recommendation_distance_enter)); //testing code!!
			//recommendationTextView.setText(getString(R.string.recommendation_enter)+" "+recommend.recommend(this)+" "+getString(R.string.text_view_recommendation_time_enter)); //original code!!
			int auxTimer = (int)(Float.parseFloat(recommend.recommend(this)))*60000;
			timeTimerAux = auxTimer;
			timer = new CounterClass(auxTimer,1000);
			chronometer.setVisibility(View.GONE);
		}
		if(MainActivity.activityOption.equals(weightLossOption)){
			//We need to estimate velocity and calories every minute or so
			recommendationTextView.setText(getString(R.string.recommendation_enter)+" "+WeightLossActivity.activityDistance+" "+ getString(R.string.text_view_recommendation_distance_km_enter)+" "+(WeightLossActivity.activityTime)*60+" "+getString(R.string.text_view_recommendation_time_enter));
			countDownTextView.setVisibility(View.GONE);  //the timer is only visible in the runner method
		}
		
	}
	
//calls the calorie counter function
	private Runnable timedTask = new Runnable(){

		  @Override
		  public void run() {
			  calculateCalories();
			  handler.postDelayed(timedTask, CALORIE_UPDATE_INTERVAL);
		  }
	 };
	 
	 private Runnable soundTask = new Runnable(){
		 @Override
		  public void run() {
			 soundHandler.postDelayed(soundTask, MINUTE_INTERVAL);
			if(minuteSoundCheckBox.isChecked())
				minuteSound.start();
			
			if(fasterSoundCheckBox.isChecked()){
				if(MainActivity.activityOption.equals(walkerOption)){
					if(calculateVelocity()<user.getAverageWalkingSpeed()) //use the average walking speed for a person
						goFasterSound.start();
				}
				else{ //we will use the same rule for both runner and weightloss
					float averageRunningSpeed = (float) ((user.getAverageRunningSpeed())*3.6); //(convert it to km/h)
					if(calculateVelocity()<averageRunningSpeed)//either use the half of the average running speed OR the running speed
						goFasterSound.start();
					
				}
			}
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
		//distance is in meters.
		return (currentDistanceInterval/10)*kmHConstant; //by the end of the calculation, we convert it to km/h
	}

	void calculateCalories(){
		setCurrentDistance();
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
				if(MainActivity.activityOption.equals(runnerOption)) //we need to reset the timer
					timer.start();
				
				resetSteps(v);
				

			}
		});
		
		startPedometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				enableAccelerometerListening();
				
				if(MainActivity.activityOption.equals(runnerOption))
					timer.start();
				chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
				chronometer.start();
				startPedometerButton.setEnabled(false);
				if(!MainActivity.activityOption.equals(runnerOption)) //pause button is not activated for the runner
					pausePedometerButton.setEnabled(true);
				finishPedometerButton.setEnabled(true);
				resetButton.setEnabled(true);
				handler.post(timedTask); //start handler (to calculate calories & other stuff)
				soundHandler.post(soundTask);
			}
		});
		pausePedometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//figure out a way to pause the timer
				//	if(MainActivity.activityOption.equals(runnerOption))
				//	timer.
				
				sensorManager.unregisterListener(sensorEventListener);
				timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
				chronometer.stop();
				handler.removeCallbacks(timedTask);
				soundHandler.removeCallbacks(soundTask);
				
				startPedometerButton.setEnabled(true);
				pausePedometerButton.setEnabled(false);
				
				
			}
		});
		finishPedometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				finishPedometerButton.setEnabled(false);
				   float time = (SystemClock.elapsedRealtime() - chronometer.getBase())/60000;
					sensorManager.unregisterListener(sensorEventListener);
					timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
					chronometer.stop();
					handler.removeCallbacks(timedTask);
					soundHandler.removeCallbacks(soundTask);

					
					
					resetButton.setEnabled(false);
					startPedometerButton.setEnabled(false);
					pausePedometerButton.setEnabled(false);
					finishPedometerButton.setEnabled(false);
				
					float recommendationAux;
					if(time>1 && distance>0){ // we will only save everything in the database if the user has more than a minute done and if he walked anything
						if(MainActivity.activityOption.equals(walkerOption)){
							
							MonitorObserver.updateWalk(context); 
							analyse = new WalkingAnalyse(context);
							analyse.enterActivity(time, distance, calories.getCalories()); //this is what to do whenever inserting a new activity
							recommend = new WalkingRecommend(context);
							recommendationAux = Math.round(Float.parseFloat(recommend.recommend(context)));
							recommendationTextView.setText(getString(R.string.text_view_next_recommendation_enter)+" "+recommendationAux+" "+getString(R.string.text_view_recommendation_distance_enter));
						}
						if(MainActivity.activityOption.equals(runnerOption)){
							timer.cancel();//we need to stop the timer
							MonitorObserver.updateRun(context);
							analyse = new RunningAnalyse(context);
							analyse.enterActivity(time, distance, calories.getCalories());
							recommend = new RunningRecommend(context);
							recommendationTextView.setText(getString(R.string.text_view_next_recommendation_enter)+" "+recommend.recommend(context)+" "+getString(R.string.text_view_recommendation_time_enter));
							timer.cancel(); //cancel the timer
							countDownTextView.setVisibility(View.GONE);
						}	
						if(MainActivity.activityOption.equals(weightLossOption)){
							MonitorObserver.updateWeightLoss();
							analyse = new WeighLossAnalyse(context);
							analyse.enterActivity(time, distance, calories.getCalories());
							recommend = new WeightLossRecommend(context);
							recommendationTextView.setText(getString(R.string.text_view_next_recommendation_enter)+" "+recommend.recommend(context)+getString(R.string.text_view_recommendation_calories_enter));
	
						}
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
		distance=0;
		calories.setCalories(0);
		stepsTextView.setText(getString(R.string.estimated_steps)+" "+String.valueOf(numSteps)+"\n"+ getString(R.string.estimated_distance) +distance+"\n"+getString(R.string.estimated_calories)+calories.getCalories());
		
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
	
	
	//class for the timer
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)  
	@SuppressLint("NewApi")
	public class CounterClass extends CountDownTimer 
	{ 
		public CounterClass(long millisInFuture, long countDownInterval) { 
			super(millisInFuture, countDownInterval); 
		} 
		
		@Override 
		public void onFinish() { 
			countDownTextView.setText(getString(R.string.completed_message)); 
			timerEndedSound.start(); //we need to make the sound the indicate the person that its done
			timer.cancel();
			//send all the stuff to the database when it ends:
			MonitorObserver.updateWalk(context); 
			analyse = new WalkingAnalyse(context);
			analyse.enterActivity(timeTimerAux, distance, calories.getCalories()); //this is what to do whenever inserting a new activity
			recommend = new WalkingRecommend(context);
			recommendationTextView.setText(getString(R.string.text_view_next_recommendation_enter)+" "+recommend.recommend(context)+" "+getString(R.string.text_view_recommendation_distance_enter));
		} 
		
		@SuppressLint("NewApi") 
		@TargetApi(Build.VERSION_CODES.GINGERBREAD) 
		@Override public void onTick(long millisUntilFinished) { 
			long millis = millisUntilFinished; 
			String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), 
					TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), 
					TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))); 
			System.out.println(hms); 
			countDownTextView.setText(hms); 
		} 
	} 
	
	  @Override
	    public void onPause() {
	        super.onPause();
	        handler.removeCallbacks(timedTask);
	        soundHandler.removeCallbacks(soundTask);
	    }

	   @Override
	    public void onStop() {
	        super.onStop();
	        handler.removeCallbacks(timedTask);
	        soundHandler.removeCallbacks(soundTask);
	    }
}
