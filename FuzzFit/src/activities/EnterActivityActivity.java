package activities;

import monitor.MonitorObserver;
import recommend.Recommend;
import recommend.RunningRecommend;
import recommend.WalkingRecommend;

import com.example.jfitnessfunctiontester.R;
import com.example.jfitnessfunctiontester.R.layout;
import com.example.jfitnessfunctiontester.R.menu;

import analyse.Analyse;
import analyse.RunningAnalyse;
import analyse.WalkingAnalyse;
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
	
	TextView recommendationTextView;
	
	EditText distanceEditText;
	EditText timeEditText;
	String distanceString="";
	String timeString="";
	
	Button enterButton;
	Button pedometerButton;
	
	Recommend recommend;
	Analyse analyse;
	
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter);
		
		context = this;
		
		recommendationTextView = (TextView) findViewById(R.id.recommendationTextView);
		
		distanceEditText = (EditText) findViewById(R.id.distanceEditText);
		distanceEditText.addTextChangedListener(distanceTextWatcher);
		timeEditText = (EditText) findViewById(R.id.timeEditText);
		timeEditText.addTextChangedListener(timeTextWatcher);
		
		enterButton = (Button) findViewById(R.id.activityEnterButton);
		pedometerButton = (Button) findViewById(R.id.pedometerButton);
		setButtons();
		
		//we need to set the kind of activity we are doing here:
		if(MainActivity.activityOption.equals(walkerOption)){
			recommend = new WalkingRecommend(this);
			recommendationTextView.setText("Recommendation: "+recommend.recommend(this)+" meters");			
		}
		if(MainActivity.activityOption.equals(runnerOption)){
			recommend = new RunningRecommend(this);
			recommendationTextView.setText("Recommendation: "+recommend.recommend(this)+" minutes");
		}
	}

	void setButtons(){
		enterButton.setEnabled(false);
		
		enterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(MainActivity.activityOption.equals(walkerOption)){
					MonitorObserver.updateWalk(context); 
					analyse = new WalkingAnalyse(context);
					analyse.enterActivity(Float.parseFloat(timeString), Float.parseFloat(distanceString), 0); //this is what to do whenever inserting a new activity
					recommend = new WalkingRecommend(context);
					recommendationTextView.setText("Recommendation: "+recommend.recommend(context)+" meters");
				
				}
				if(MainActivity.activityOption.equals(runnerOption)){
					MonitorObserver.updateRun(context);
					analyse = new RunningAnalyse(context);
					analyse.enterActivity(Float.parseFloat(timeString), Float.parseFloat(distanceString), 0);
					recommend = new RunningRecommend(context);
					recommendationTextView.setText("Recommendation: "+recommend.recommend(context)+" minutes");
					
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
	}
	
	
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
