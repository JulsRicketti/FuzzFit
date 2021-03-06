package activities;

import history.History;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.fuzzfit.R;

import persistance.DatabaseAdapter;
import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ReportListViewActivity extends ListActivity{
	
	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";

	
	Button plotsButton;
	Button exportDatabaseButton;
	
	History history;
	ArrayList<String> dates;
	ArrayList<String> calories;
	ArrayList<String> distance;
	

	
	Context context = this;
	
	String[] list_view_test;
	
	ListView reportListView;
	
	String table ="";
	
	TextView record1TextView;
	TextView record2TextView;
	TextView record3TextView;
	TextView record4TextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_report_list_view);

			//set screen title
			if(ReportMenuActivity.reportOption.equals(walkerOption))
				setTitle( getString(R.string.report_walker_title));
			if(ReportMenuActivity.reportOption.equals(runnerOption))
				setTitle( getString(R.string.report_runner_title));
			if(ReportMenuActivity.reportOption.equals(weightLossOption))
				setTitle( getString(R.string.report_weight_loss_title));

		    
		    
		    history = new History(context);
		    
		    plotsButton = (Button) findViewById(R.id.plotsButton);
		    exportDatabaseButton = (Button) findViewById(R.id.exportDatabaseButton);
		    setButtons();
		    reportListView = (ListView) findViewById(android.R.id.list);

		    ArrayList<String> al = new ArrayList<String>();
		    
		    record1TextView = (TextView) findViewById(R.id.record1TextView);
		    record2TextView = (TextView) findViewById(R.id.record2TextView);
		    record3TextView = (TextView) findViewById(R.id.record3TextView);
		    record4TextView = (TextView) findViewById(R.id.record4TextView);
		    
		    if(ReportMenuActivity.reportOption.equals(walkerOption)){
		    	table = DatabaseAdapter.WALKER_HISTORY_TABLE;
		    	dates = history.getWalkerHistory().getActivityDate();
		    	distance = history.getWalkerHistory().activityDistance;
		    }

		    if(ReportMenuActivity.reportOption.equals(runnerOption)){
		    	table = DatabaseAdapter.RUNNER_HISTORY_TABLE;
		    	dates = history.getRunnerHistory().getActivityDate();
		    	distance = history.getRunnerHistory().activityDistance;
		    }
		    
		    if(ReportMenuActivity.reportOption.equals(weightLossOption)){
		    	table = DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE;
		    	dates = history.getWeightLossHistory().getActivityDate();
		    	calories = history.getWeightLossHistory().getCalories();
		    }
		    
		    al=history.getHistory(table).activityDate;
		    final ArrayAdapter<String> ad;
		    ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,al);
		    reportListView.setAdapter(ad);
		    
		    //set the textviews for the records
		    record1TextView.setText(getString(R.string.record_distance)+" "+getBiggestItem(history.getHistory(table).activityDistance)+" "+getString(R.string.toast_activity_distance_unit));
		    record2TextView.setText(getString(R.string.record_time)+" "+getBiggestItem(history.getHistory(table).activityTime)+" "+getString(R.string.toast_activity_time_unit));
		    record3TextView.setText(getString(R.string.record_average_velocity)+" "+getBiggestItem(history.getHistory(table).activityVelocity)+" "+getString(R.string.toast_activity_average_velocity_unit));
		    record4TextView.setText(getString(R.string.record_calories)+" "+getBiggestItem(history.getHistory(table).calories));
		
	}

	
	float getBiggestItem(ArrayList<String> arrayList){
		ArrayList<Float> arrayListFloat = new ArrayList<Float>();
		
		//we "convert" all of the string items into float to find the largest
			for(int i=0; i<arrayList.size(); i++){
				try{
					arrayListFloat.add(Float.parseFloat(arrayList.get(i)));
				}
				catch (NumberFormatException n){
					String aux = arrayList.get(i).replace(",", ".");
					arrayListFloat.add(Float.parseFloat(aux));
				}
			}
		Collections.sort(arrayListFloat);
		
		return arrayListFloat.get(arrayListFloat.size()-1);
	}
	 
	void setButtons(){
		plotsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ReportListViewActivity.this, ViewGraphActivity.class);
				startActivity(i);
			}
		});
		
		exportDatabaseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email="";
				try {
					File sdCard = Environment.getExternalStorageDirectory();
					File dir = new File (sdCard.getAbsolutePath() + "/FuzzFitDocuments");
					dir.mkdirs();
					File myFile = new File(dir, ReportMenuActivity.reportOption+"fuzzFitDataFile.txt");
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					
					email += ReportMenuActivity.reportOption+" History:\n";
					myOutWriter.append(ReportMenuActivity.reportOption+" History:\n");											
					for(int i =0; i<history.getHistory(table).activityDate.size(); i++){
						myOutWriter.append(" Date: "+history.getHistory(table).activityDate.get(i));
						email += " Date: "+history.getHistory(table).activityDate.get(i);
						myOutWriter.append(" Recommendation: "+history.getHistory(table).recommendation.get(i));
						email+="\n Recommendation: "+history.getHistory(table).recommendation.get(i);
						myOutWriter.append(" Distance: "+history.getHistory(table).activityDistance.get(i));
						email+="\n Distance: "+history.getHistory(table).activityDistance.get(i);
						myOutWriter.append(" Time: "+history.getHistory(table).activityTime.get(i));
						email+="\n Time: "+history.getHistory(table).activityTime.get(i);
						myOutWriter.append(" Average Velocity: "+history.getHistory(table).activityVelocity.get(i));
						email +="\n Average Velocity: "+history.getHistory(table).activityVelocity.get(i);
						myOutWriter.append(" Monitor: "+history.getHistory(table).monitor.get(i));
						email+="\n Monitor: "+history.getHistory(table).monitor.get(i);
						myOutWriter.append(" Calories: "+history.getHistory(table).calories.get(i)+"\n");
						email+="\n Calories: "+history.getHistory(table).calories.get(i)+"\n";
					}
					//records:
					myOutWriter.append("\nRecords:");
					email +="\nRecords:";
					myOutWriter.append("\nRecord Distance: "+getBiggestItem(history.getHistory(table).activityDistance));
					email +="\nRecord Distance: "+getBiggestItem(history.getHistory(table).activityDistance);
					myOutWriter.append("\nRecord time: "+getBiggestItem(history.getHistory(table).activityTime));
					email+="\nRecord time: "+getBiggestItem(history.getHistory(table).activityTime);
					myOutWriter.append("\nRecord average velocity: "+getBiggestItem(history.getHistory(table).activityVelocity));
					email +="\nRecord average velocity: "+getBiggestItem(history.getHistory(table).activityVelocity);
					myOutWriter.append("\nRecord burned calories: "+getBiggestItem(history.getHistory(table).calories));
					email +="\nRecord burned calories: "+getBiggestItem(history.getHistory(table).calories);
					myOutWriter.close();
					fOut.close();
					sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(Uri.fromFile(myFile)));
					Toast.makeText(getBaseContext(),
							"Done writing SD "+ReportMenuActivity.reportOption+"fuzzFitDataFile.txt",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
				//email
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"thefuzzfamilyprojects@gmail.com"});
				i.putExtra(Intent.EXTRA_SUBJECT, "Activity Data");
				i.putExtra(Intent.EXTRA_TEXT   , email);
				try {
				    startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
			}
						
		});
	}
	
	//this is for whenever we click on the item
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Toast.makeText(this, getString(R.string.toast_activity_date)+" "+history.getHistory(table).activityDate.get(position)+
				"\n"+getString(R.string.toast_activity_recommendation)+" "+history.getHistory(table).recommendation.get(position)+
				"\n"+getString(R.string.toast_activity_distance)+" "+history.getHistory(table).activityDistance.get(position)+" "+getString(R.string.toast_activity_distance_unit)+
				"\n"+getString(R.string.toast_activity_time)+" "+history.getHistory(table).activityTime.get(position)+" "+getString(R.string.toast_activity_time_unit)+
				"\n"+getString(R.string.toast_activity_average_velocity)+" "+history.getHistory(table).activityVelocity.get(position)+" "+getString(R.string.toast_activity_average_velocity_unit)+
				"\n"+getString(R.string.toast_activity_monitor)+" "+history.getHistory(table).monitor.get(position)+
				"\n"+getString(R.string.toast_activity_calories)+" "+history.getHistory(table).calories.get(position), Toast.LENGTH_LONG).show();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report_list_view, menu);
		return true;
	}

}
