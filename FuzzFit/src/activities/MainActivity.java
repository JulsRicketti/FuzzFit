package activities;


import java.lang.annotation.RetentionPolicy;

import com.example.jfitnessfunctiontester.Mediator;
import com.example.jfitnessfunctiontester.R;
import com.example.jfitnessfunctiontester.R.id;
import com.example.jfitnessfunctiontester.R.layout;
import com.example.jfitnessfunctiontester.R.menu;
import com.example.jfitnessfunctiontester.User;

import history.History;
import monitor.Monitor;
import monitor.MonitorObserver;
import monitor.WalkingMonitor;
import persistance.DatabaseAdapter;
import recommend.Recommend;
import recommend.WalkingRecommend;
import analyse.Analyse;
import analyse.RunningAnalyse;
import analyse.WalkingAnalyse;
import android.app.Activity;
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

public class MainActivity extends Activity {
	
	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	public static String activityOption =""; //will always let me know which kind of activity I am going for.
	
	//Stuff for the forms:
	Button enterButton;
	Button runnerButton;
	
	String distance="0", time="0";
	
    DatabaseAdapter db = new DatabaseAdapter(this);
	//ActivityHistory activityHistory = new ActivityHistory();
	History history;
	Mediator mediator;
	Recommend recommend;
	Analyse analyse;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		enterButton = (Button)findViewById(R.id.enterTestButton);
		enterButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				//doStuff(); //for testing purposes
				activityOption = walkerOption;
				goToActivity();

			}
		});
		
		runnerButton = (Button) findViewById(R.id.runnerButton);
		runnerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activityOption = runnerOption;
				goToActivity();
				
			}
		});
		
		db.open();
		db.deleteAll(DatabaseAdapter.WALKER_HISTORY_TABLE);
		db.deleteAll(DatabaseAdapter.RUNNER_HISTORY_TABLE);
		history = new History(this);
	}
	
	void goToActivity(){
		Intent i  = new Intent(MainActivity.this,EnterActivityActivity.class);
		startActivity(i);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}