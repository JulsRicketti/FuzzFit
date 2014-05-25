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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ViewGraphActivity extends Activity {

	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	final String walkerTitle = "Walk Training Report";
	final String runnerTitle = "Run Training Report";
	final String weightLossTitle = "Weight Loss Program Report";
	
	private GraphicalView mChart;
	
//	Button exportGraphButton; //(at the moment, its deleted)
	
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
		
		//set screen title
		if(ReportMenuActivity.reportOption.equals(walkerOption))
			setTitle(walkerTitle+ " Chart");
		if(ReportMenuActivity.reportOption.equals(runnerOption))
			setTitle(runnerTitle+ " Chart");
		if(ReportMenuActivity.reportOption.equals(weightLossOption))
			setTitle(weightLossTitle+ " Chart");

		
//		exportGraphButton =(Button) findViewById(R.id.exportGraphButton);
		setButtons();
		
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

	ImageView bmImage; 
	LinearLayout chartContainer;
	
	void setButtons(){
//		exportGraphButton.setOnClickListener(new OnClickListener() {
			
//			@Override
//			public void onClick(View v) {
//				bmImage = (ImageView)findViewById(R.id.image);
//
//				chartContainer.setDrawingCacheEnabled(true);
//			      // this is the important code :)  
//			      // Without it the view will have a dimension of 0,0 and the bitmap will be null          
//
//				chartContainer.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 
//			            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//
//
//				chartContainer.buildDrawingCache(true);
//			      Bitmap b = Bitmap.createBitmap(chartContainer.getDrawingCache());
//			      chartContainer.setDrawingCacheEnabled(false); // clear drawing cache
//
//			      bmImage.setImageBitmap(b);
				
//			}
//		});
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
	        TimeSeries velocitySeries = new TimeSeries("Average Velocity (km/h)");
	        TimeSeries caloriesSeries = new TimeSeries("Calories Burned");
	 
	        // Adding distanceData to Visits and Views Series
	        for(int i=0;i<dt.length;i++){
	            distanceSeries.add(dt[i], distance[i]);
	            timeSeries.add(dt[i],time[i]);
	            velocitySeries.add(dt[i], velocity[i]);
	            caloriesSeries.add(dt[i], calories[i]);
	            
	        }
	 
	        // Creating a dataSet to hold each series
	        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
	        dataSet.addSeries(distanceSeries);
	        dataSet.addSeries(timeSeries);
	        dataSet.addSeries(velocitySeries);
	        dataSet.addSeries(caloriesSeries);
	 
	        // Creating XYSeriesRenderer to customize distanceSeries
	        XYSeriesRenderer distanceRenderer = new XYSeriesRenderer();
	        distanceRenderer.setColor(Color.parseColor("#990000"));
	        distanceRenderer.setPointStyle(PointStyle.DIAMOND);
	        distanceRenderer.setFillPoints(true);
	        distanceRenderer.setLineWidth(5);
	        distanceRenderer.setDisplayChartValues(true);
	        distanceRenderer.setChartValuesTextSize(20);
	 
	        // Creating XYSeriesRenderer to customize timeSeries
	        XYSeriesRenderer timeRenderer = new XYSeriesRenderer();
	        timeRenderer.setColor(Color.parseColor("#0000AA"));
	        timeRenderer.setPointStyle(PointStyle.CIRCLE);
	        timeRenderer.setFillPoints(true);
	        timeRenderer.setLineWidth(5);
	        timeRenderer.setDisplayChartValues(true);
	        timeRenderer.setChartValuesTextSize(20);
	        
	        XYSeriesRenderer velocityRenderer = new XYSeriesRenderer();
	        velocityRenderer.setColor(Color.parseColor("#559900"));
	        velocityRenderer.setPointStyle(PointStyle.SQUARE);
	        velocityRenderer.setFillPoints(true);
	        velocityRenderer.setLineWidth(5);
	        velocityRenderer.setDisplayChartValues(true);
	        velocityRenderer.setChartValuesTextSize(20);
	 
	        XYSeriesRenderer caloriesRenderer = new XYSeriesRenderer();
	        caloriesRenderer.setColor(Color.parseColor("#9900AA"));
	        caloriesRenderer.setPointStyle(PointStyle.TRIANGLE);
	        caloriesRenderer.setFillPoints(true);
	        caloriesRenderer.setLineWidth(5);
	        caloriesRenderer.setDisplayChartValues(true);
	        caloriesRenderer.setChartValuesTextSize(20);
	        
	        // Creating a XYMultipleSeriesRenderer to customize the whole chart
	        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
	 
	        multiRenderer.setChartTitle("Improvement Chart");
	        multiRenderer.setChartTitleTextSize(40);
	        multiRenderer.setLabelsColor(Color.BLACK);
	        multiRenderer.setXTitle("Days");
	        multiRenderer.setXLabelsColor(Color.BLACK);
	        multiRenderer.setAxisTitleTextSize(20);
	        multiRenderer.setLegendTextSize(25);
	        multiRenderer.setYTitle("Count");
	        multiRenderer.setYLabelsColor(0,Color.BLACK);
	        multiRenderer.setLabelsTextSize(30);
	        multiRenderer.setZoomButtonsVisible(true);
	        multiRenderer.setBackgroundColor(Color.parseColor("#F5F5F5"));
	        multiRenderer.setMarginsColor(Color.parseColor("#F5F5F5"));
	        multiRenderer.setShowGrid(true);
	        multiRenderer.setGridColor(Color.GRAY);
	        multiRenderer.setPointSize(10.0f);
	        multiRenderer.setMargins(new int[] { 50, 50, 50, 22 }); //thing that fixes the legend (the 3rd one is the one to change)
	        
	        
	        multiRenderer.addSeriesRenderer(distanceRenderer);
	        multiRenderer.addSeriesRenderer(timeRenderer);
	        multiRenderer.addSeriesRenderer(velocityRenderer);
	        multiRenderer.addSeriesRenderer(caloriesRenderer);

	        
	 
	        // Getting a reference to LinearLayout of the MainActivity Layout
	        chartContainer = (LinearLayout) findViewById(R.id.chart_container);
	 
	        // Creating a Time Chart
	        mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataSet, multiRenderer,"dd-MMM-yyyy");
	 
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
