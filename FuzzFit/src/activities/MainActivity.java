package activities;

import others.Mediator;
import others.User;

import com.example.jfitnessfunctiontester.R;


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
	
	Button deleteDBButton; //used only for testing!
	Button insertDataButton; //also used only for testing (inserts fake data)
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
		
		deleteDBButton = (Button) findViewById(R.id.deleteDBButton);
		deleteDBButton.setVisibility(View.GONE);
		insertDataButton = (Button) findViewById(R.id.insertDataButton);
		insertDataButton.setVisibility(View.GONE);
		
		
		setButtons();
		db.open();
		//db.deleteAll(DatabaseAdapter.USER_PROFILE_TABLE); //just for testing remember to remove!!

		if(db.userProfileIsEmpty()){
			walkerButton.setEnabled(false);
			runnerButton.setEnabled(false);
			weightLossButton.setEnabled(false);
			reportButton.setEnabled(false);
			Toast.makeText(getApplicationContext(), "Please register yourself before we start.", Toast.LENGTH_LONG).show();
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
		
		deleteDBButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				db.open();
				db.deleteAll(DatabaseAdapter.RUNNER_HISTORY_TABLE);
				db.deleteAll(DatabaseAdapter.WALKER_HISTORY_TABLE);
				db.deleteAll(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE);
				db.close();
			}
		});
		
		insertDataButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				db.open();
				
				//delete everything we have first
				db.deleteAll(DatabaseAdapter.RUNNER_HISTORY_TABLE);
				db.deleteAll(DatabaseAdapter.WALKER_HISTORY_TABLE);
				db.deleteAll(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE);
				
				//walker:
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-01", "800", "60", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-02", "900", "60", "13", "91", "130");
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-03", "800", "60", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-04", "1000", "60", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-05", "900", "60", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-06", "1123", "60", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-07", "911", "60", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-08", "800", "60", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-09", "1234", "60", "13", "96", "100");				
				db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-10", "231", "60", "13", "96", "100");
				
				//runner
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "10", "2014-05-01", "1400", "10", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "15", "2014-05-01", "1000", "15", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "15", "2014-05-01", "1000", "15", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "20", "2014-05-01", "1300", "15", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "20", "2014-05-02", "900", "15", "13", "91", "130");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "20", "2014-05-03", "800", "20", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "25", "2014-05-04", "1000", "20", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "25", "2014-05-05", "900", "25", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "30", "2014-05-06", "1123", "30", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "30", "2014-05-07", "911", "30", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "30", "2014-05-08", "800", "30", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "30", "2014-05-09", "1500", "35", "13", "96", "100");				
				db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "30", "2014-05-10", "1400", "35", "13", "96", "100");
				
				//weightloss
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-01", "1000", "10", "13", "96", "100");
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-02", "900", "15", "13", "91", "150");
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-03", "800", "20", "13", "96", "150");
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-04", "1000", "20", "13", "96", "200");
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-05", "900", "25", "13", "96", "250");
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-06", "1123", "30", "13", "96", "300");
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-07", "911", "30", "13", "96", "350");
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-08", "800", "30", "13", "96", "400");
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-09", "1500", "35", "13", "96", "450");				
				db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "400", "2014-05-10", "1400", "35", "13", "96", "500");

				db.close();
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