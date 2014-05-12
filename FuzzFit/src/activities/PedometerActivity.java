package activities;

import java.util.Timer;
import java.util.TimerTask;

import monitor.MonitorObserver;
import recommend.Recommend;
import recommend.RunningRecommend;
import recommend.WalkingRecommend;

import com.example.jfitnessfunctiontester.R;
import com.example.jfitnessfunctiontester.R.id;
import com.example.jfitnessfunctiontester.R.layout;
import com.example.jfitnessfunctiontester.R.menu;
import com.example.jfitnessfunctiontester.User;


import analyse.Analyse;
import analyse.RunningAnalyse;
import analyse.WalkingAnalyse;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
	
	static final int CALORIE_UPDATE_INTERVAL = 10000;

	
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
	
	TextView xTextView;
	TextView yTextView;
	TextView zTextView; 
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedometer);
		
		User user = new User(this);
		
		if(user.getSex()=="Male")
			strideLength = Float.parseFloat(user.getHeight())*maleStrideLengthConstant;
		else
			strideLength = Float.parseFloat(user.getHeight())*femaleStrideLengthConstant;
		
		xTextView = (TextView) findViewById(R.id.xTextView);
		yTextView = (TextView) findViewById(R.id.yTextView);
		zTextView = (TextView) findViewById(R.id.zTextView);
		
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

//Error here!
		//Possible solution here:
		//http://stackoverflow.com/questions/17379002/java-lang-runtimeexception-cant-create-handler-inside-thread-that-has-not-call
//		Timer calorieCounterTimer = new Timer();
//		
//		TimerTask calorieCounterTask = new TimerTask() {
//			
//			@Override
//			public void run() {
//			//	Toast.makeText(context, "TCCII - APROVADO\n Situação: APROVADO", Toast.LENGTH_LONG).show();
//			}
//		};
//		
//		calorieCounterTimer.schedule(calorieCounterTask, CALORIE_UPDATE_INTERVAL);
		
		
		//setting stuff related to physical activies:
		recommendationTextView =(TextView) findViewById(R.id.pedometerRecommendationTextView);
		if(MainActivity.activityOption.equals(walkerOption)){
			recommend = new WalkingRecommend(this);
			recommendationTextView.setText("Recommendation: "+recommend.recommend(this)+" meters");			
		}
		if(MainActivity.activityOption.equals(runnerOption)){
			recommend = new RunningRecommend(this);
			recommendationTextView.setText("Recommendation: "+recommend.recommend(this)+" minutes");
		}
		if(MainActivity.activityOption.equals(weightLossOption)){
			//We need to estimate velocity and calories every minute or so
			
		}
	}

	
	void setButtons(){
		finishPedometerButton.setEnabled(false);
		
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
				finishPedometerButton.setEnabled(true);
				chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
				chronometer.start();
				
			}
		});
		pausePedometerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sensorManager.unregisterListener(sensorEventListener);
				timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
				chronometer.stop();
				
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
				    
					if(MainActivity.activityOption.equals(walkerOption)){
						MonitorObserver.updateWalk(context); 
						analyse = new WalkingAnalyse(context);
						analyse.enterActivity(time, distance, 0); //this is what to do whenever inserting a new activity
						recommend = new WalkingRecommend(context);
						recommendationTextView.setText("Next Recommendation: "+recommend.recommend(context)+" meters");
					
					}
					if(MainActivity.activityOption.equals(runnerOption)){
						MonitorObserver.updateRun(context);
						analyse = new RunningAnalyse(context);
						analyse.enterActivity(time, distance, 0);
						recommend = new RunningRecommend(context);
						recommendationTextView.setText("Next Recommendation: "+recommend.recommend(context)+" minutes");
						
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
				stepsTextView.setText("Steps: "+String.valueOf(numSteps)+"\nDistance: "+distance);
			}
			xTextView.setText(String.valueOf(x));
			yTextView.setText(String.valueOf(y));
			zTextView.setText(String.valueOf(z));
			
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
