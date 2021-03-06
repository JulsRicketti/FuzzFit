package activities;

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

import com.fuzzfit.R;

import persistance.DatabaseAdapter;
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
import android.widget.CheckBox;
import android.widget.ImageView;
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
	
	//For testing purposes only:
	ArrayList<String> recommendationData;
	ArrayList<String> monitorData;
	
	Context context = this;
	
	CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_graph);
		

		//set screen title
		if(ReportMenuActivity.reportOption.equals(walkerOption))
			setTitle( getString(R.string.report_walker_title));
		if(ReportMenuActivity.reportOption.equals(runnerOption))
			setTitle( getString(R.string.report_runner_title));
		if(ReportMenuActivity.reportOption.equals(weightLossOption))
			setTitle( getString(R.string.report_weight_loss_title));

		
		history = new History(context);

	    if(ReportMenuActivity.reportOption.equals(walkerOption)){ //(distanceData)
	    	dates = history.getWalkerHistory().getActivityDate();
	    	distanceData = history.getWalkerHistory().activityDistance;
	    	timeData = history.getWalkerHistory().activityTime;
	    	velocityData = history.getWalkerHistory().activityVelocity;
	    	caloriesData = history.getWalkerHistory().calories;
	    	
	    	//(testing purposes only!)
	    	recommendationData = history.getWalkerHistory().recommendation;
	    	monitorData = history.getWalkerHistory().monitor;
	    	
	    	
	    }

	    if(ReportMenuActivity.reportOption.equals(runnerOption)){//(distanceData)
	    	dates = history.getRunnerHistory().getActivityDate();
	    	distanceData = history.getRunnerHistory().activityDistance;
	    	timeData = history.getRunnerHistory().activityTime;
	    	velocityData = history.getRunnerHistory().activityVelocity;
	    	caloriesData = history.getRunnerHistory().calories;
	    	
	    	//(testing purposes only!)
	    	recommendationData = history.getRunnerHistory().recommendation;
	    	monitorData = history.getRunnerHistory().monitor;

	    }
	    
	    if(ReportMenuActivity.reportOption.equals(weightLossOption)){//(calories)
	    	dates = history.getWeightLossHistory().getActivityDate();
	    	distanceData = history.getWeightLossHistory().activityDistance;
	    	timeData = history.getWeightLossHistory().activityTime;
	    	velocityData = history.getWeightLossHistory().activityVelocity;
	    	caloriesData = history.getWeightLossHistory().calories;
	    	
	    	//(testing purposes only!)
	    	recommendationData = history.getWeightLossHistory().recommendation;
	    	monitorData = history.getWeightLossHistory().monitor;

	    }
		openChart();
		
		//setting the checkboxes (by default all of them are checked)
		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		checkBox1.setChecked(true);
		checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
		checkBox2.setChecked(true);
		checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
		checkBox3.setChecked(true);
		checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
		checkBox4.setChecked(true);
		setCheckBoxes();
	}

	ImageView bmImage; 
	LinearLayout chartContainer;
	
	void setCheckBoxes(){
		checkBox1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((CheckBox)v).isChecked())
					distanceRenderer.setColor(Color.parseColor("#990000")); //(making transparent method)
				else
					distanceRenderer.setColor(Color.TRANSPARENT); //(making transparent method)
				mChart.repaint();
			}
		});
		
		checkBox2.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((CheckBox)v).isChecked())
					timeRenderer.setColor(Color.parseColor("#0000AA")); //(making transparent method)
				else
					timeRenderer.setColor(Color.TRANSPARENT); //(making transparent method)
				mChart.repaint();
				
			}
		});
		checkBox3.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((CheckBox)v).isChecked())
					velocityRenderer.setColor(Color.parseColor("#559900")); //(making transparent method)
				else
					velocityRenderer.setColor(Color.TRANSPARENT); //(making transparent method)
				mChart.repaint();
				
			}
		});
		
		checkBox4.setOnClickListener( new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				if(((CheckBox)v).isChecked())
					caloriesRenderer.setColor(Color.parseColor("#9900AA")); //(making transparent method)
				else
					caloriesRenderer.setColor(Color.TRANSPARENT); //(making transparent method)
				mChart.repaint();
			
		}
});

		
	}
	
	private class GraphParameters{
		public int lineWidth;
		public int valuesTextSize;
		public int axisTitleTextSize;
		public int legendTextSize;
		public int chartTitleTextSize;
		public int chartLabelTextSize;
		public float pointSize;
		public int []margins;
	}
	
	GraphParameters graphParameters = new GraphParameters();
	
	void setGraphSizes(){
        if(findViewById(R.id.graph_view_layout).getTag().equals("small_screen")){
        	graphParameters.lineWidth = 2;
        	graphParameters.valuesTextSize =8;
        	graphParameters.chartTitleTextSize = 10;
        	graphParameters.axisTitleTextSize = 10;
        	graphParameters.legendTextSize = 8;
        	graphParameters.chartLabelTextSize = 8;
        	graphParameters.pointSize = 3.0f;
        	graphParameters.margins = new int[]{10, 20, 50, 0 };
        }
        if(findViewById(R.id.graph_view_layout).getTag().equals("normal_screen") ||findViewById(R.id.graph_view_layout).getTag().equals("default_screen")){
        	graphParameters.lineWidth = 3;
        	graphParameters.valuesTextSize =15;
        	graphParameters.chartTitleTextSize = 20;
        	graphParameters.axisTitleTextSize = 12;
        	graphParameters.legendTextSize = 15;
        	graphParameters.chartLabelTextSize = 15;
        	graphParameters.pointSize = 3.0f;
        	graphParameters.margins = new int[]{10, 20, 50, 0 };
        }
        if(findViewById(R.id.graph_view_layout).getTag().equals("large_screen") ||findViewById(R.id.graph_view_layout).getTag().equals("xlarge_screen")){
        	graphParameters.lineWidth = 5;
        	graphParameters.valuesTextSize =20;
        	graphParameters.chartTitleTextSize = 40;
        	graphParameters.axisTitleTextSize = 20;
        	graphParameters.legendTextSize = 25;
        	graphParameters.chartLabelTextSize = 30;
        	graphParameters.pointSize = 10.0f;
        	graphParameters.margins = new int[]{50, 50, 50, 22 };
        	
        }
	}
	
	XYMultipleSeriesDataset dataSet;
	XYMultipleSeriesRenderer multiRenderer; //need to keep it out so the checkboxes can see it
	XYSeriesRenderer distanceRenderer;
	XYSeriesRenderer timeRenderer;
	XYSeriesRenderer velocityRenderer;
	XYSeriesRenderer caloriesRenderer;
	private void openChart(){
		setGraphSizes(); //(here we set the parameters for each screen size) 
		
		
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
					
					//Commented these for testing purposes only!!
					try{
						velocity[i] =  Float.parseFloat(velocityData.get(i));
					}
					catch (Exception exp){
						String aux = velocityData.get(i).replace(",", ".");
						velocity[i] = Float.parseFloat(aux);
					}
					calories[i] = Float.parseFloat(caloriesData.get(i));
					//this is just for recommendation!!
//					try{
//					velocity[i] = Math.round(Float.parseFloat(recommendationData.get(i-1)));
//					}
//					catch (Exception exp){
//						velocity[i] = 1740;
//					}
//					calories[i] = Math.round(Float.parseFloat(monitorData.get(i)));

					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	 
	        // Creating TimeSeries for Visits
	        TimeSeries distanceSeries = new TimeSeries(getString(R.string.series_distance));
	        TimeSeries timeSeries = new TimeSeries(getString(R.string.series_time));
	        TimeSeries velocitySeries = new TimeSeries(getString(R.string.series_velocity));
	        TimeSeries caloriesSeries = new TimeSeries(getString(R.string.series_calories));
//	        TimeSeries velocitySeries = new TimeSeries("Recomendação (metros)");
//	        TimeSeries caloriesSeries = new TimeSeries("Resultado do Monitor (%)");
	 
	        // Adding distanceData to Visits and Views Series
	        for(int i=0;i<dt.length;i++){
	            distanceSeries.add(dt[i], distance[i]);
	            timeSeries.add(dt[i],time[i]);
	            velocitySeries.add(dt[i], velocity[i]);
	            caloriesSeries.add(dt[i], calories[i]);
	            
	        }
	 
	        // Creating a dataSet to hold each series
	        dataSet = new XYMultipleSeriesDataset();
	        dataSet.addSeries(distanceSeries);
	        dataSet.addSeries(timeSeries);
	        dataSet.addSeries(velocitySeries);
	        dataSet.addSeries(caloriesSeries);
	 
	        // Creating XYSeriesRenderer to customize distanceSeries
	        distanceRenderer = new XYSeriesRenderer();
	        distanceRenderer.setColor(Color.parseColor("#990000"));
	        distanceRenderer.setPointStyle(PointStyle.DIAMOND);
	        distanceRenderer.setFillPoints(true);
	        distanceRenderer.setLineWidth(graphParameters.lineWidth);
	        distanceRenderer.setDisplayChartValues(true);
	        distanceRenderer.setChartValuesTextSize(graphParameters.valuesTextSize);
	 
	        // Creating XYSeriesRenderer to customize timeSeries
	        timeRenderer = new XYSeriesRenderer();
	        timeRenderer.setColor(Color.parseColor("#0000AA"));
	        timeRenderer.setPointStyle(PointStyle.CIRCLE);
	        timeRenderer.setFillPoints(true);
	        timeRenderer.setLineWidth(graphParameters.lineWidth);
	        timeRenderer.setDisplayChartValues(true);
	        timeRenderer.setChartValuesTextSize(graphParameters.valuesTextSize);
	        
	        velocityRenderer = new XYSeriesRenderer();
	        velocityRenderer.setColor(Color.parseColor("#559900"));
	        velocityRenderer.setPointStyle(PointStyle.SQUARE);
	        velocityRenderer.setFillPoints(true);
	        velocityRenderer.setLineWidth(graphParameters.lineWidth);
	        velocityRenderer.setDisplayChartValues(true);
	        velocityRenderer.setChartValuesTextSize(graphParameters.valuesTextSize);
	 
	        caloriesRenderer = new XYSeriesRenderer();
	        caloriesRenderer.setColor(Color.parseColor("#9900AA"));
	        caloriesRenderer.setPointStyle(PointStyle.TRIANGLE);
	        caloriesRenderer.setFillPoints(true);
	        caloriesRenderer.setLineWidth(graphParameters.lineWidth);
	        caloriesRenderer.setDisplayChartValues(true);
	        caloriesRenderer.setChartValuesTextSize(graphParameters.valuesTextSize);
	        
	        // Creating a XYMultipleSeriesRenderer to customize the whole chart
	        multiRenderer = new XYMultipleSeriesRenderer();
	 
	        multiRenderer.setChartTitle(getString(R.string.chart_name));
	        multiRenderer.setChartTitleTextSize(graphParameters.chartTitleTextSize);
	        multiRenderer.setLabelsColor(Color.BLACK);
	        multiRenderer.setXTitle("Dias"); //testing purposes only (change it back to Days!)
	        multiRenderer.setXLabelsColor(Color.BLACK);
	        multiRenderer.setAxisTitleTextSize(graphParameters.axisTitleTextSize);
	        multiRenderer.setLegendTextSize(graphParameters.legendTextSize);
	        multiRenderer.setYTitle(" "); //testing purposes only (change it back to Count!)
	        multiRenderer.setYLabelsColor(0,Color.BLACK);
	        multiRenderer.setLabelsTextSize(graphParameters.chartLabelTextSize);
	        multiRenderer.setZoomButtonsVisible(true);
	        multiRenderer.setBackgroundColor(Color.parseColor("#F5F5F5"));
	        multiRenderer.setMarginsColor(Color.parseColor("#F5F5F5"));
	        multiRenderer.setShowGrid(true);
	        multiRenderer.setGridColor(Color.GRAY);
	        multiRenderer.setPointSize(graphParameters.pointSize);
	        multiRenderer.setMargins(graphParameters.margins); //thing that fixes the legend (the 3rd one is the one to change)
	        
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
	                    String selectedSeries=getString(R.string.selected_series_distance);
	                    if(seriesIndex==0)
	                        selectedSeries = getString(R.string.selected_series_distance);
	                    if(seriesIndex==1)
	                        selectedSeries = getString(R.string.selected_series_time);
	                    if(seriesIndex==2)
	                        selectedSeries = getString(R.string.selected_series_velocity);
	                    if(seriesIndex==3)
	                        selectedSeries = getString(R.string.selected_series_calories);
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
