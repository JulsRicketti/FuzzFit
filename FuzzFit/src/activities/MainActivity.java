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
//				db.open();
//////			delete everything we have first
//			db.deleteAll(DatabaseAdapter.WALKER_HISTORY_TABLE);
//			db.deleteAll(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE);
//			db.deleteAll(DatabaseAdapter.RUNNER_HISTORY_TABLE);
//
//			//Data for my runner:
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-11", "1570.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-13", "1583.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-14", "1529.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-15", "1533.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-18", "1535.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-19", "1604.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-20", "1556.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-21", "1518.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-22", "1560.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-23", "1619.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-24", "1601.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-26", "1680.0", "10.0", "0.82", "79.3", "100.0");
////			//(data for the table)
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-27", "1613.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-06-28", "1636.0", "10.0", "0.82", "79.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-07-01", "1728.0", "10.0", "0.82", "89.1", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-07-02", "1668.0", "10.0", "0.82", "81.7", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "1740", "2014-07-04", "1712.0", "10.0", "0.82", "86.6", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "2088", "2014-07-05", "1783.0", "10.0", "0.82", "91.3", "100.0");
//			db.insertActivity(DatabaseAdapter.RUNNER_HISTORY_TABLE, "2088", "2014-07-06", "1964.0", "10.0", "0.82", "81.7", "100.0");
//			
//			//inconsistent data for walker:
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "785", "2014-05-26", "680.0", "50.0", "0.82", "60.125797", "100.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "863.5", "2014-05-27", "800.0", "60.0", "0.80", "91.33333", "120.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "863.5", "2014-05-28", "800", "60.0", "0.80", "77.8907", "120.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "863.5", "2014-06-03", "800.0", "45.0", "1.07", "63.56881", "90.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "949.85", "2014-06-03", "1000.0", "60.0", "1.00", "91.33333", "120.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1044.835", "2014-06-04", "1000.0", "60.0", "1.00", "91.33333", "120.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1044.835", "2014-06-06", "1000.0", "60.0", "1.00", "81.46147", "120.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1149.3185", "2014-06-09", "1100.0", "70.0", "0.94", "91.33333", "140.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1264.2504", "2014-06-10", "4000.0", "120.0", "2.00", "91.33333", "240.0");				
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1390.6754", "2014-06-11", "1500.0", "60.0", "1.50", "91.33333", "120.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1529.7429", "2014-06-12", "1500.0", "80.0", "1.12", "91.33333", "160.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1376.7687", "2014-06-13", "800.0", "30.0", "1.60", "38.737583", "60.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1376.7687", "2014-06-14", "1000.0", "60.0", "1.00", "70.095795", "120.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1376.7687", "2014-06-16", "1000.0", "60.0", "1.00", "70.095795", "120.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1514.4456", "2014-06-17", "1500.0", "60.0", "1.50", "91.33333", "120.0");
////			
//			//consistent data for walker:
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "863.5", "2014-06-01", "2800.0", "36.0", "4.67", "91.33333", "126.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "949.85", "2014-06-02", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1044.835", "2014-06-03", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1149.3185", "2014-06-04", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1264.2504", "2014-06-05", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1390.6754", "2014-06-08", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1529.7429", "2014-06-09", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1682.7172", "2014-06-10", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "1850.9889", "2014-06-11", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "2036.0878", "2014-06-15", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "2239.6965", "2014-06-16", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "2463.6663", "2014-06-17", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "2710.033", "2014-06-18", "4000.0", "50.0", "4.80", "91.33333", "175.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "2981.0361", "2014-06-20", "4500.0", "60.0", "4.50", "91.33333", "210.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "3279.1396", "2014-06-23", "4500.0", "60.0", "4.50", "91.33333", "210.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "3607.0537", "2014-06-24", "4500.0", "60.0", "4.50", "91.33333", "210.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "3967.759", "2014-06-25", "4500.0", "60.0", "4.50", "91.33333", "210.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "4364.535", "2014-06-26", "4500.0", "60.0", "4.50", "91.33333", "210.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "4800.989", "2014-06-27", "4500.0", "60.0", "4.50", "91.33333", "210.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "4800.989", "2014-06-28", "4500.0", "60.0", "4.50", "78.971275", "210.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "4800.989", "2014-06-29", "4500.0", "60.0", "4.50", "78.971275", "210.0");
//			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "5281.088", "2014-06-30", "5000.0", "60.0", "5.00", "91.33333", "210.0");
//			
////			Consistent data WITH modification (calories and speed are wrong!!)
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "3500", "2014-06-30", "2800.0", "36.0", "4.67", "91.3", "126.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "3500", "2014-07-01", "4011.0", "50.0", "4.67", "69.4", "126.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "3850", "2014-07-02", "4092.0", "60.0", "4.67", "91.3", "126.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "4235", "2014-07-03", "4056.0", "60.0", "4.67", "91.3", "126.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "4658", "2014-07-04", "4377.0", "68.0", "4.67", "91.3", "126.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "4192", "2014-07-06", "4087.0", "52.0", "4.67", "59.3", "126.0");
////			db.insertActivity(DatabaseAdapter.WALKER_HISTORY_TABLE, "4192", "2014-07-07", "4028.0", "58.0", "4.67", "70.3", "126.0");
////			
////			//WeightLoss Data (Christine)
//			db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "100.0", "2014-06-13", "437.0", "11.0", "2.38", "36.49996", "22.0");
//			db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "150.0", "2014-06-13", "3208.0", "67.0", "2.87", "91.33333", "134.0");
//			db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "200.0", "2014-06-19", "3224.0", "61.0", "3.17", "91.33333", "122.0");
//			db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "200.0", "2014-06-21", "2300.0", "67.0", "2.06", "72.84691", "134.0");
//			db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "250.0", "2014-06-22", "4719.0", "78.0", "3.63", "91.33333", "234.0");
//			db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "250.0", "2014-06-23", "2792.0", "39.0", "4.30", "64.1214", "117.0");
//			db.insertActivity(DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE, "250.0", "2014-06-23", "896.0", "15.0", "3.58", "36.5", "117.0");
//			db.close();
//
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