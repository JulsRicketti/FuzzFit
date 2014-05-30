package activities;


import java.util.ArrayList;

import com.fuzzfit.R;

import persistance.DatabaseAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterUserActivity extends Activity {

	DatabaseAdapter dbAdapter;
	
	Button finishRegisterButton;
	
	EditText ageRegisterEditText;
	EditText weightRegisterEditText;
	EditText heightRegisterEditText;
	
	
	Spinner sexRegisterSpinner;
	ArrayList<String> sexSpinnerList = new ArrayList<String>();
	
	String age = "";
	String weight = "";
	String height = "";
	String sex = "Select Sex";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		
		setTitle(getString(R.string.register_user_title));
		
		dbAdapter = new DatabaseAdapter(this);
		dbAdapter = dbAdapter.open();
		
		ageRegisterEditText = (EditText)findViewById(R.id.ageRegisterEditText);
		ageRegisterEditText.addTextChangedListener(ageTextWatcher);
		weightRegisterEditText = (EditText) findViewById(R.id.weightRegisterEditText);
		weightRegisterEditText.addTextChangedListener(weightTextWatcher);
		heightRegisterEditText = (EditText) findViewById(R.id.heightRegisterEditText);
		heightRegisterEditText.addTextChangedListener(heightTextWatcher);
		
		sexRegisterSpinner = (Spinner)findViewById(R.id.sexRegisterSpinner);
		setSpinner();

		finishRegisterButton = (Button)findViewById(R.id.finishRegisterButton);
		setButton();
		

	}
	void setSpinner(){
		sexSpinnerList.add(getString(R.string.select_sex));
		sexSpinnerList.add(getString(R.string.select_sex_male));
		sexSpinnerList.add(getString(R.string.select_sex_female));
		ArrayAdapter<String> sexArrayAdapter = new ArrayAdapter<String>(RegisterUserActivity.this,android.R.layout.simple_spinner_item,sexSpinnerList);
		sexRegisterSpinner.setAdapter(sexArrayAdapter);
		
		sexRegisterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				sex = sexRegisterSpinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	}
	
	void setButton(){
		finishRegisterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!sex.equals("Select Sex") && !weight.equals("") && !height.equals("") && !age.equals("")){
					dbAdapter.deleteAll(DatabaseAdapter.USER_PROFILE_TABLE); //we make sure only one is made for this application
					if(sex.equals("Masculino")) //I need to translate the sex for further use in the app
						sex = "Male";
					dbAdapter.insertUser(age, weight, height, sex);
//					Toast.makeText(getApplicationContext(), "Done!  "+dbAdapter.getAllUserRecords().getCount(), Toast.LENGTH_SHORT).show();
					Intent i  = new Intent(RegisterUserActivity.this,MainActivity.class);
					startActivity(i);
				}
				else{
					Toast.makeText(getApplicationContext(), "Please fill out the whole form", Toast.LENGTH_SHORT).show();
				}

			}
		});
		
	}
	
	TextWatcher ageTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			age = s.toString();
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
	
	
	TextWatcher weightTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			weight = s.toString();
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
	
	TextWatcher heightTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			height = s.toString();
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_user, menu);
		return true;
	}




}
