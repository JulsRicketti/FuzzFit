package activities;

import com.fuzzfit.R;

import others.Mediator;
import others.User;



import history.History;
import persistance.DatabaseAdapter;
import recommend.Recommend;
import analyse.Analyse;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	public static String activityOption =""; //will always let me know which kind of activity I am going for.
	
	//Stuff for the forms:
	Button walkerButton;
	Button runnerButton;
	Button weightLossButton;
	
	Button userProfileButton;
	
	Button reportButton;
	
//	Button deleteDBButton; //used only for testing!
//	Button insertDataButton; //also used only for testing (inserts fake data)
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

				
		userProfileButton = (Button) findViewById(R.id.userProfileButton);
		
		reportButton = (Button) findViewById(R.id.reportButton);
		
		walkerButton = (Button)findViewById(R.id.enterTestButton);
		runnerButton = (Button) findViewById(R.id.runnerButton);
		weightLossButton = (Button) findViewById(R.id.weightLossButton);
		
//		deleteDBButton = (Button) findViewById(R.id.deleteDBButton);
//		deleteDBButton.setVisibility(View.GONE);
//		insertDataButton = (Button) findViewById(R.id.insertDataButton);
//		insertDataButton.setVisibility(View.GONE);
		
		
		setButtons();
		db.open();
//		db.deleteAll(DatabaseAdapter.USER_PROFILE_TABLE); //just for testing remember to remove!!		
//		db.deleteAll(DatabaseAdapter.RUNNER_HISTORY_TABLE);
//		db.deleteAll(DatabaseAdapter.WALKER_HISTORY_TABLE);
//		db.deleteAll(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE);
		if(db.userProfileIsEmpty()){
			walkerButton.setEnabled(false);
			runnerButton.setEnabled(false);
			weightLossButton.setEnabled(false);
			reportButton.setEnabled(false);
			Toast.makeText(getApplicationContext(), getString(R.string.before_start), Toast.LENGTH_LONG).show();
		}
		else{
			walkerButton.setEnabled(true);
			runnerButton.setEnabled(true);
			weightLossButton.setEnabled(true);
			reportButton.setEnabled(true);
		}

		db.close();
		
	}
	
	void setButtons(){
		userProfileButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent i  = new Intent(MainActivity.this,RegisterUserActivity.class);
				startActivity(i);
				//For testing purposes, this will now th be the button to insert data into the graphs:
				
			}
		});
		
		walkerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//doStuff(); //for testing purposes
				activityOption = walkerOption;
				Intent i  = new Intent(MainActivity.this,EnterActivityActivity.class);
				startActivity(i);

			}
		});
		
		runnerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activityOption = runnerOption;
				Intent i  = new Intent(MainActivity.this,EnterActivityActivity.class);
				startActivity(i);
				
			}
		});
		
		weightLossButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activityOption = weightLossOption;
				Intent i = new Intent(MainActivity.this, WeightLossActivity.class);
				startActivity(i);
				
			}
		});
		
		reportButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//remember to change this back to ReportMenuActivity! For now this is testing purposes ONLY!
				Intent i = new Intent(MainActivity.this, ReportMenuActivity.class);
				startActivity(i);
				
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}