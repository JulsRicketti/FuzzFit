package activities;

import history.History;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.jfitnessfunctiontester.R;

public class ReportMenuActivity extends Activity {

	public static String reportOption ="";
	
	static final String walkerOption = "WALKER";
	static final String runnerOption = "RUNNER";
	static final String weightLossOption = "WEIGHT_LOSS";
	
	Button walkerReportButton;
	Button runnerReportButton;
	Button weightLossReportButton;
	
	History history;
	Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_menu);
		
		setTitle("Report Menu");
		
		walkerReportButton = (Button) findViewById(R.id.walkerReportButton);
		runnerReportButton = (Button) findViewById(R.id.runnerReportButton);
		weightLossReportButton = (Button) findViewById(R.id.weightLossReportButton);
		
		setButtons();
		
		history = new History(context);
		
		//we only allow to access reports that already have entries
		if(history.isWalkerEmpty())
			walkerReportButton.setEnabled(false);
		if(history.isRunnerEmpty())
			runnerReportButton.setEnabled(false);
		if(history.isWeightLossEmpty())
			weightLossReportButton.setEnabled(false);

	}

	void setButtons(){
		walkerReportButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reportOption = walkerOption;
				Intent i = new Intent(ReportMenuActivity.this, ReportListViewActivity.class);
				startActivity(i);
			}
		});
		
		runnerReportButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reportOption = runnerOption;
				Intent i = new Intent(ReportMenuActivity.this, ReportListViewActivity.class);
				startActivity(i);

			}
		});
		
		weightLossReportButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reportOption = weightLossOption;
				Intent i = new Intent(ReportMenuActivity.this, ReportListViewActivity.class);
				startActivity(i);

				
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report_menu, menu);
		return true;
	}


}
