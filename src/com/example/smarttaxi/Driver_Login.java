package com.example.smarttaxi;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Driver_Login extends Activity implements OnClickListener {
	Button btn_cancel,btn_log;
	private String android_id; 
	private EditText phone;
	private JSONObject result;
	private SQLiteDatabase myDB= null;
	private Cursor c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_login);
		android_id = Secure.getString(this.getContentResolver(),
	            Secure.ANDROID_ID); 
		myDB = this.openOrCreateDatabase("SmartTaxi", MODE_PRIVATE, null);
		
		btn_cancel=(Button) findViewById(R.id.driver_login_cancel);
		btn_log=(Button) findViewById(R.id.driver_login_login);
		btn_cancel.setOnClickListener(this);
		btn_log.setOnClickListener(this);
		phone=(EditText) findViewById(R.id.driver_login_phone);
	}
	@Override
	public void onClick(View v) {
		Log.d("mylog", "WORKS");
		int id = v.getId();
		if (id == R.id.driver_login_cancel) {
			finish();
		} else if (id == R.id.driver_login_login) {
			Log.d("mylog", "WORKS1");
			if (phone.length()==10){
			Login login=new Login();
			JSONObject jo=new JSONObject();
			try {
				
				jo.put("user_uuid", android_id);
				jo.put("user_role", "2");
				jo.put("user_phone", phone.getText().toString());
				result=login.execute(jo).get();
				Log.d("mylog", "RRR:"+result.getInt("success"));
				Log.d("mylog", result.toString());
				Log.d("mylog", result.getString("success"));
				Toast.makeText(getApplicationContext(), "Okay! It is :"+result.getInt("success"), Toast.LENGTH_LONG).show();
				if (result.getInt("success")==2)
				{
					Intent intent=new Intent(this, Validation.class);
					intent.putExtra("json", result.toString());
					startActivity(intent);
					finish();
				} else if (result.getInt("success")==1){
					myDB.execSQL("INSERT INTO Active"
						     + " (_id, user_uuid,user_phone,user_fname,user_lname,driver_img," +
						     "driver_car_id,driver_car_brand,driver_car_model,driver_car_color,user_role)"
						     + " VALUES ("+1+",'"
						     +result.getString("user_uuid")+"','"
						     +result.getString("user_phone")+"','" 
						     +result.getString("user_fname")+"','"
						     +result.getString("user_lname")+"','"
						     +result.getString("user_img")+"','"
						     +result.getString("driver_car_id")+"','"
						     +result.getString("driver_car_brand")+"','"
						     +result.getString("driver_car_model")+"','"
						     +result.getString("driver_car_color")+"',"
						     +result.getInt("user_role")+");");
					Intent intent=new Intent(this,Driver_Main.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
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
			else {
				Toast.makeText(this, "������������ �����", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
}
