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
	ArrayList<String> distanceData;
	ArrayList<String> timeData;
	ArrayList<String> velocityData;
	ArrayList<String> caloriesData;
	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_graph);
		
		history = new History(context);

	    if(ReportMenuActivity.reportOption.equals(walkerOption)){ //(distanceData)
	    	dates = history.getWalkerHistory().getActivityDate();
	    	distanceData = history.getWalkerHistory().activityDistance;
	    	timeData = history.getWalkerHistory().activityTime;
	    	velocityData = history.getWalkerHistory().activityVelocity;
	    	caloriesData = history.getWalkerHistory().calories;
	    }

	    if(ReportMenuActivity.reportOption.equals(runnerOption)){//(distanceData)
	    	dates = history.getRunnerHistory().getActivityDate();
	    	distanceData = history.getRunnerHistory().activityDistance;
	    	timeData = history.getRunnerHistory().activityTime;
	    	velocityData = history.getRunnerHistory().activityVelocity;
	    	caloriesData = history.getRunnerHistory().calories;
	    }
	    
	    if(ReportMenuActivity.reportOption.equals(weightLossOption)){//(calories)
	    	dates = history.getWeightLossHistory().getActivityDate();
	    	distanceData = history.getWeightLossHistory().activityDistance;
	    	timeData = history.getWeightLossHistory().activityTime;
	    	velocityData = history.getWeightLossHistory().activityVelocity;
	    	caloriesData = history.getWeightLossHistory().calories;

	    }
		openChart();
	}

	 private void openChart(){
		 
	        Date[] dt = new Date[dates.size()];
	        float[] distance = new float[dates.size()];
	        float[] velocity = new float[dates.size()];
	        float[] calories = new float[dates.size()];
	        float[] time = new float[dates.size()];
	        for(int i=0;i<dates.size();i++){
	        	try {
					dt[i]= new SimpleDateFormat("yyyy-MM-dd").parse(dates.get(i));
					distance[i] = Float.parseFloat(distanceData.get(i));
					time[i] = Float.parseFloat(timeData.get(i));
					velocity[i] =  Float.parseFloat(velocityData.get(i));
					calories[i] = Float.parseFloat(caloriesData.get(i));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	 
	        // Creating TimeSeries for Visits
	        TimeSeries distanceSeries = new TimeSeries("Distance (meters)");
	        TimeSeries timeSeries = new TimeSeries("Time (minutes)");
	        TimeSeries velocitySeries = new TimeSeries("Average Velocity (m/s)");
	        TimeSeries caloriesSeries = new TimeSeries("Calories Burned (kcal)");
	 
	        // Adding distanceData to Visits and Views Series
	        for(int i=0;i<dt.length;i++){
	            distanceSeries.add(dt[i], distance[i]);
	            timeSeries.add(dt[i],time[i]);
	            velocitySeries.add(dt[i], velocity[i]);
	            caloriesSeries.add(dt[i], calories[i]);
	            
	        }
	 
	        // Creating a distanceDataset to hold each series
	        XYMultipleSeriesDataset distanceDataset = new XYMultipleSeriesDataset();
	 
	        // Adding Visits Series to the distanceDataset
	        distanceDataset.addSeries(distanceSeries);
	        distanceDataset.addSeries(timeSeries);
	        distanceDataset.addSeries(velocitySeries);
	        distanceDataset.addSeries(caloriesSeries);
	 
	        // Creating XYSeriesRenderer to customize distanceSeries
	        XYSeriesRenderer distanceRenderer = new XYSeriesRenderer();
	        distanceRenderer.setColor(Color.RED);
	        distanceRenderer.setPointStyle(PointStyle.DIAMOND);
	        distanceRenderer.setFillPoints(true);
	        distanceRenderer.setLineWidth(7);
	        distanceRenderer.setDisplayChartValues(true);
	 
	        // Creating XYSeriesRenderer to customize timeSeries
	        XYSeriesRenderer timeRenderer = new XYSeriesRenderer();
	        timeRenderer.setColor(Color.BLUE);
	        timeRenderer.setPointStyle(PointStyle.CIRCLE);
	        timeRenderer.setFillPoints(true);
	        timeRenderer.setLineWidth(7);
	        timeRenderer.setDisplayChartValues(true);
	        
	        XYSeriesRenderer velocityRenderer = new XYSeriesRenderer();
	        velocityRenderer.setColor(Color.GREEN);
	        velocityRenderer.setPointStyle(PointStyle.SQUARE);
	        velocityRenderer.setFillPoints(true);
	        velocityRenderer.setLineWidth(7);
	        velocityRenderer.setDisplayChartValues(true);
	 
	        XYSeriesRenderer caloriesRenderer = new XYSeriesRenderer();
	        caloriesRenderer.setColor(Color.MAGENTA);
	        caloriesRenderer.setPointStyle(PointStyle.TRIANGLE);
	        caloriesRenderer.setFillPoints(true);
	        caloriesRenderer.setLineWidth(7);
	        caloriesRenderer.setDisplayChartValues(true);
	        
	        // Creating a XYMultipleSeriesRenderer to customize the whole chart
	        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
	 
	        multiRenderer.setChartTitle("Improvement Chart");
	        multiRenderer.setXTitle("Days");
	        multiRenderer.setYTitle("Count");
	        multiRenderer.setZoomButtonsVisible(true);
	 
	        // Adding distanceRenderer and timeRenderer to multipleRenderer
	        // Note: The order of adding distanceDataseries to distanceDataset and renderers to multipleRenderer
	        // should be same
	        multiRenderer.addSeriesRenderer(distanceRenderer);
	        multiRenderer.addSeriesRenderer(timeRenderer);
	        multiRenderer.addSeriesRenderer(velocityRenderer);
	        multiRenderer.addSeriesRenderer(caloriesRenderer);
	 
	        // Getting a reference to LinearLayout of the MainActivity Layout
	        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
	 
	        // Creating a Time Chart
	        mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), distanceDataset, multiRenderer,"dd-MMM-yyyy");
	 
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
	                    String selectedSeries="Distance";
	                    if(seriesIndex==0)
	                        selectedSeries = "Distance";
	                    if(seriesIndex==1)
	                        selectedSeries = "Time";
	                    if(seriesIndex==2)
	                        selectedSeries = "Velocity";
	                    if(seriesIndex==3)
	                        selectedSeries = "Calories";
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
