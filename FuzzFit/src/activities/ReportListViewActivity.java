package activities;

import history.History;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import persistance.DatabaseAdapter;
import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.example.jfitnessfunctiontester.R;
import com.example.jfitnessfunctiontester.ViewGraphActivity;

public class ReportListViewActivity extends ListActivity{
	
	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	Button plotsButton;
	
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

		    history = new History(context);
		    
		    plotsButton = (Button) findViewById(R.id.plotsButton);
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
		    record1TextView.setText("Record Distance: "+getBiggestItem(history.getHistory(table).activityDistance));
		    record2TextView.setText("Record time: "+getBiggestItem(history.getHistory(table).activityTime));
		    record3TextView.setText("Record average velocity: "+getBiggestItem(history.getHistory(table).activityVelocity));
		    record4TextView.setText("Record burned calories: "+getBiggestItem(history.getHistory(table).calories));
		
	}

	
	float getBiggestItem(ArrayList<String> arrayList){
		ArrayList<Float> arrayListFloat = new ArrayList<Float>();
		
		//we "convert" all of the string items into float to find the largest
		for(int i=0; i<arrayList.size(); i++)
			arrayListFloat.add(Float.parseFloat(arrayList.get(i)));
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
	}
	
	//this is for whenever we click on the item
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
	      Toast.makeText(this, "Activity Date: "+history.getHistory(table).activityDate.get(position)+
	      "\nActivity Recommendation: "+history.getHistory(table).recommendation.get(position)+ //see how I can choose
	      "\nActivity Distance: "+history.getHistory(table).activityDistance.get(position)+" meters"+
	      "\nActivity Time: "+history.getHistory(table).activityTime.get(position)+" minutes"+
	      "\nActivity Average Velocity: "+history.getHistory(table).activityVelocity.get(position)+ "meters/second"+
	      "\nActivity Monitor: "+history.getHistory(table).monitor.get(position)+
	      "\nActivity Calories: "+history.getHistory(table).calories.get(position), Toast.LENGTH_LONG).show();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report_list_view, menu);
		return true;
	}

}
