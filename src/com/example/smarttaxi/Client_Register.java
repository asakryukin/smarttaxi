package com.example.smarttaxi;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Client_Register extends Activity implements OnClickListener {
	Button btn_cancel,btn_reg;
	EditText phone,name;
	private String android_id;
	JSONObject result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_register);
		android_id = Secure.getString(this.getContentResolver(),
	            Secure.ANDROID_ID); 
		btn_cancel=(Button) findViewById(R.id.client_register_cancel);
		btn_reg=(Button) findViewById(R.id.client_register_register);
		btn_cancel.setOnClickListener(this);
		btn_reg.setOnClickListener(this);
		
		phone=(EditText) findViewById(R.id.driver_register_carcolor);
		name=(EditText) findViewById(R.id.client_register_name);
	}
	@Override
	public void onClick(View v) {
		Log.d("mylog", "WORKS");
		int id = v.getId();
		if (id == R.id.client_register_cancel) {
			finish();
		} else if (id == R.id.client_register_register) {
			Log.d("mylog", "WORKS1");
			
			Register reg=new Register();
			JSONObject jo=new JSONObject();
			try {
				
				jo.put("user_uuid", android_id);
				jo.put("user_fname", name.getText().toString());
				jo.put("user_phone", phone.getText().toString());
				jo.put("user_role", "1");
				/*jo.put("user_uuid", "abcd");
				jo.put("user_fname", "asdfasdf");
				jo.put("user_phone", "7083539174");
				jo.put("user_role", "1");*/
				result=reg.execute(jo).get();
				if (result.getInt("success")==0){
					Toast.makeText(this, result.getString("message"), Toast.LENGTH_LONG).show();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("mylog", jo.toString());
			
			
		}
	}
	
}
