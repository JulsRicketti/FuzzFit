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
		    reportListView.setAdapter(ad); //exception here
		
	}

	float getBiggestItem(ArrayList<String> arrayList){
		ArrayList<Float> arrayListFloat = new ArrayList<Float>();
		
		//we "convert" all of the string items into float to find the largest
		for(int i=0; i<arrayList.size(); i++)
			arrayListFloat.add(Float.parseFloat(arrayList.get(i)));
		Collections.sort(arrayListFloat);
		
		return arrayListFloat.get(arrayListFloat.size()-1);
	}
	
	void popupWindow(){
		 LayoutInflater layoutInflater 
	     = (LayoutInflater)getBaseContext()
	      .getSystemService(LAYOUT_INFLATER_SERVICE);  
	    View popupView = layoutInflater.inflate(R.layout.activity_report_popup_layout, null);  
	             final PopupWindow popupWindow = new PopupWindow(
	               popupView, 
	               LayoutParams.WRAP_CONTENT,  
	                     LayoutParams.WRAP_CONTENT);  
	            openChart();
	             Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
	             btnDismiss.setOnClickListener(new Button.OnClickListener(){
	            
	     @Override
	     public void onClick(View v) {
	      // TODO Auto-generated method stub
	      popupWindow.dismiss();
	     }});
	               
	             popupWindow.showAsDropDown(plotsButton, 50, -30);
	         
	   }
	
	private GraphicalView mChart;
	
	void setButtons(){
		plotsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				popupWindow();
			//	openChart();
			}
		});
	}

	 private void openChart(){
		 
	        Date[] dt = new Date[dates.size()];
	        float[] visits = new float[dates.size()];
	        float[] views = new float[dates.size()];
	        for(int i=0;i<dates.size();i++){
	        	try {
					dt[i]= new SimpleDateFormat("yyyy-MM-dd").parse(dates.get(i));
					visits[i] = Float.parseFloat(distance.get(i));
					views[i] = Float.parseFloat(distance.get(i));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	 
//	        int[] visits = { 2000,2500,2700,2100,2800};
//	        int[] views = {2200, 2700, 2900, 2800, 3200};
	 
	        // Creating TimeSeries for Visits
	        TimeSeries visitsSeries = new TimeSeries("Visits");
	 
	        // Creating TimeSeries for Views
	        TimeSeries viewsSeries = new TimeSeries("Views");
	 
	        // Adding data to Visits and Views Series
	        for(int i=0;i<dt.length;i++){
	            visitsSeries.add(dt[i], visits[i]);
	            viewsSeries.add(dt[i],views[i]);
	        }
	 
	        // Creating a dataset to hold each series
	        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	 
	        // Adding Visits Series to the dataset
	        dataset.addSeries(visitsSeries);
	 
	        // Adding Visits Series to dataset
	        dataset.addSeries(viewsSeries);
	 
	        // Creating XYSeriesRenderer to customize visitsSeries
	        XYSeriesRenderer visitsRenderer = new XYSeriesRenderer();
	        visitsRenderer.setColor(Color.BLACK);
	        visitsRenderer.setPointStyle(PointStyle.CIRCLE);
	        visitsRenderer.setFillPoints(true);
	        visitsRenderer.setLineWidth(2);
	        visitsRenderer.setDisplayChartValues(true);
	 
	        // Creating XYSeriesRenderer to customize viewsSeries
	        XYSeriesRenderer viewsRenderer = new XYSeriesRenderer();
	        viewsRenderer.setColor(Color.BLUE);
	        viewsRenderer.setPointStyle(PointStyle.CIRCLE);
	        viewsRenderer.setFillPoints(true);
	        viewsRenderer.setLineWidth(2);
	        viewsRenderer.setDisplayChartValues(true);
	 
	        // Creating a XYMultipleSeriesRenderer to customize the whole chart
	        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
	 
	        multiRenderer.setChartTitle("Visits vs Views Chart");
	        multiRenderer.setXTitle("Days");
	        multiRenderer.setYTitle("Count");
	        multiRenderer.setZoomButtonsVisible(true);
	 
	        // Adding visitsRenderer and viewsRenderer to multipleRenderer
	        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
	        // should be same
	        multiRenderer.addSeriesRenderer(visitsRenderer);
	        multiRenderer.addSeriesRenderer(viewsRenderer);
	 
	        // Getting a reference to LinearLayout of the MainActivity Layout
	        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
	 
	        // Creating a Time Chart
	        mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");
	 
	        multiRenderer.setClickEnabled(true);
	        multiRenderer.setSelectableBuffer(10);
	 
	        // Setting a click event listener for the graph
	        mChart.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
	 
	                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();
	 
	                if (seriesSelection != null) {
	                    int seriesIndex = seriesSelection.getSeriesIndex();
	                    String selectedSeries="Visits";
	                    if(seriesIndex==0)
	                        selectedSeries = "Visits";
	                    else
	                        selectedSeries = "Views";
	 
	                    // Getting the clicked Date ( x value )
	                    long clickedDateSeconds = (long) seriesSelection.getXValue();
	                    Date clickedDate = new Date(clickedDateSeconds);
	                    String strDate = formatter.format(clickedDate);
	 
	                    // Getting the y value
	                    int amount = (int) seriesSelection.getValue();
	 
	                    // Displaying Toast Message
	                    Toast.makeText(
	                        getBaseContext(),
	                        selectedSeries + " on "  + strDate + " : " + amount ,
	                        Toast.LENGTH_SHORT).show();
	                    }
	                }
	            });
	 
	            // Adding the Line Chart to the LinearLayout
	            chartContainer.addView(mChart);
	    }

	
	//this is for whenever we click on the item
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
	      Toast.makeText(this, "Activity Date: "+history.getHistory(table).activityDate.get(position)+
	      "\nActivity Recommendation: "+history.getHistory(table).recommendation.get(position)+
	      "\nActivity Distance: "+history.getHistory(table).activityDistance.get(position)+
	      "\nActivity Time: "+history.getHistory(table).activityTime.get(position)+
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
