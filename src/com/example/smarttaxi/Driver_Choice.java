package com.example.smarttaxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Driver_Choice extends Activity implements OnClickListener {
	
Button login,register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_choice);
		
		login=(Button) findViewById(R.id.driver_choice_login);
		register=(Button) findViewById(R.id.driver_choice_register);
		
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		int id = v.getId();
		if (id == R.id.driver_choice_login) {
			Intent intent=new Intent(this, Driver_Login.class);
			startActivity(intent);
			finish();			
		} else if (id == R.id.driver_choice_register) {
			Intent intent=new Intent(this, Driver_Register.class);
			startActivity(intent);
			finish();
		}
		
	}

}
