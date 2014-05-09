package com.example.jfitnessfunctiontester;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WeightLossActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight_loss);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weight_loss, menu);
		return true;
	}

}
