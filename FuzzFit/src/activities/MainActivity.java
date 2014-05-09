package activities;

import com.example.jfitnessfunctiontester.Mediator;
import com.example.jfitnessfunctiontester.R;


import history.History;
import persistance.DatabaseAdapter;
import recommend.Recommend;
import analyse.Analyse;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	public static String activityOption =""; //will always let me know which kind of activity I am going for.
	
	//Stuff for the forms:
	Button walkerButton;
	Button runnerButton;
	Button weightLossButton;
	
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
		
		walkerButton = (Button)findViewById(R.id.enterTestButton);
		walkerButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				//doStuff(); //for testing purposes
				activityOption = walkerOption;
				Intent i  = new Intent(MainActivity.this,EnterActivityActivity.class);
				startActivity(i);

			}
		});
		
		runnerButton = (Button) findViewById(R.id.runnerButton);
		runnerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activityOption = runnerOption;
				Intent i  = new Intent(MainActivity.this,EnterActivityActivity.class);
				startActivity(i);
				
			}
		});
		
		weightLossButton = (Button) findViewById(R.id.weightLossButton);
		weightLossButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activityOption = weightLossOption;
				Intent i = new Intent(MainActivity.this, PedometerActivity.class);
				startActivity(i);
				
			}
		});
//this is solely for testing purposes (it can be taken out and put back on at any time)		
//		db.open();
//		db.deleteAll(DatabaseAdapter.WALKER_HISTORY_TABLE);
//		db.deleteAll(DatabaseAdapter.RUNNER_HISTORY_TABLE);
		history = new History(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}