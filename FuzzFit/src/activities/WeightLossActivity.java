package activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jfitnessfunctiontester.Mediator;
import com.example.jfitnessfunctiontester.R;

public class WeightLossActivity extends Activity {

	
	Button wlRecommendationButton;
	Button wlSwitchExerciseButton;
	Button wlEnterActivityButton;
	
	TextView caloriesRecommendationTextView;
	TextView wlExerciseOptionTextView;
	
	EditText wlCalorieConsumptionEditText;
	String calorieConsumption="";
	
	Mediator mediator; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight_loss);
		
		caloriesRecommendationTextView = (TextView) findViewById(R.id.caloriesRecommendationTextView);
		wlExerciseOptionTextView = (TextView) findViewById(R.id.wlExerciseOptionTextView);
		
		wlCalorieConsumptionEditText = (EditText) findViewById(R.id.wlCalorieConsumptionEditText);
		wlCalorieConsumptionEditText.addTextChangedListener(calorieConsumptionTextWatcher);
		
		wlRecommendationButton = (Button) findViewById(R.id.wlRecommendationButton);
		wlSwitchExerciseButton = (Button) findViewById(R.id.wlSwitchExerciseButton);
		wlEnterActivityButton = (Button) findViewById(R.id.wlEnterActivityButton);
		setButtons();
		
	}
	
	TextWatcher calorieConsumptionTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			calorieConsumption = s.toString();
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	void setButtons(){
		wlRecommendationButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(!calorieConsumption.equals("")){
					float calorieConsumptionValue = Float.parseFloat(calorieConsumption);	
					if(calorieConsumptionValue<1500){
						Toast.makeText(getApplicationContext(), "For a healthy weight loss please consume over 1500 calories daily\n (Please consult a nutritionist if necessary)", Toast.LENGTH_LONG).show();
					}
					if(calorieConsumptionValue>2500){
						Toast.makeText(getApplicationContext(), "For possibility of maximum weight loss without body damage, please consume under 2500 calories daily\n (Please consult a nutritionist if necessary)", Toast.LENGTH_LONG).show();
					}
				}
				else{ //if the user doesnt specify, we assume we are working with 2000 calories daily 
					Toast.makeText(getApplicationContext(), "By default it will be assumed your intake is of 2000 calories.\n (Please consult a nutritionist if necessary)", Toast.LENGTH_LONG).show();
				}
				
				
			}
		});
		
		wlSwitchExerciseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		wlEnterActivityButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weight_loss, menu);
		return true;
	}

}
