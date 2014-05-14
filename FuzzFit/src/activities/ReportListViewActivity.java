package activities;

import java.util.ArrayList;

import persistance.DatabaseAdapter;

import history.History;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jfitnessfunctiontester.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class ReportListViewActivity extends ListActivity{
	
	public class GraphViewData implements GraphViewDataInterface {
	    private double x,y;

	    public GraphViewData(double x, double y) {
	        this.x = x;
	        this.y = y;
	    }

	    @Override
	    public double getX() {
	        return this.x;
	    }

	    @Override
	    public double getY() {
	        return this.y;
	    }
	}
	
	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	Button plotsButton;
	
	History history;
	Context context = this;
	
	String[] list_view_test;
	
	ListView reportListView;
	
	String table ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_report_list_view);

		    history = new History(context);
		    
		    plotsButton = (Button) findViewById(R.id.plotsButton);
		    setButtons();
		    reportListView = (ListView) findViewById(android.R.id.list);

		    ArrayList<String> al = new ArrayList<String>();
		    
		    if(ReportMenuActivity.reportOption.equals(walkerOption))
		    	table = DatabaseAdapter.WALKER_HISTORY_TABLE;

		    if(ReportMenuActivity.reportOption.equals(runnerOption))
		    	table = DatabaseAdapter.RUNNER_HISTORY_TABLE;
		    
		    if(ReportMenuActivity.reportOption.equals(weightLossOption))
		    	table = DatabaseAdapter.WEIGHT_LOSS_HISTORY_TABLE;
		    
		    al=history.getHistory(table).activityDate;
		    final ArrayAdapter<String> ad;
		    ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,al);
		    reportListView.setAdapter(ad); //exception here
		
	}

	void setButtons(){
		plotsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	//			Toast.makeText(context, "Trabalho de Conclusão de Curso II - APROVADO", Toast.LENGTH_SHORT).show();
				GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
				    new GraphViewData(1, 2.0d)
				    , new GraphViewData(2, 1.5d)
				    , new GraphViewData(3, 2.5d)
				    , new GraphViewData(4, 1.0d)
				});
				 
				GraphView graphView = new LineGraphView(context, "GraphViewDemo");
				graphView.addSeries(exampleSeries); // data
				 
				LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
				layout.addView(graphView);
			}
		});
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
