package com.example.jfitnessfunctiontester;

import history.History;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import activities.ReportListViewActivity;
import activities.ReportMenuActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ViewGraphActivity extends Activity {

	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	private GraphicalView mChart;
	
	History history;
	ArrayList<String> dates;
	ArrayList<String> data;
	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_graph);
		
		history = new History(context);

	    if(ReportMenuActivity.reportOption.equals(walkerOption)){ //(data)
	    	dates = history.getWalkerHistory().getActivityDate();
	    	data = history.getWalkerHistory().activityDistance;
	    }

	    if(ReportMenuActivity.reportOption.equals(runnerOption)){//(data)
	    	dates = history.getRunnerHistory().getActivityDate();
	    	data = history.getRunnerHistory().activityDistance;
	    }
	    
	    if(ReportMenuActivity.reportOption.equals(weightLossOption)){//(calories)
	    	dates = history.getWeightLossHistory().getActivityDate();
	    	data = history.getWeightLossHistory().getCalories();
	    }
		openChart();
	}

	 private void openChart(){
		 
	        Date[] dt = new Date[dates.size()];
	        float[] visits = new float[dates.size()];
	        float[] views = new float[dates.size()];
	        for(int i=0;i<dates.size();i++){
	        	try {
					dt[i]= new SimpleDateFormat("yyyy-MM-dd").parse(dates.get(i));
					visits[i] = Float.parseFloat(data.get(i));
					views[i] = Float.parseFloat(data.get(i));
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
	 
	        if(ReportMenuActivity.reportOption.equals(walkerOption) || ReportMenuActivity.reportOption.equals(runnerOption))
	        	multiRenderer.setChartTitle("Distance Improvement Chart");
	        else
	        	multiRenderer.setChartTitle("Calories Burned Chart");
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_graph, menu);
		return true;
	}

}
