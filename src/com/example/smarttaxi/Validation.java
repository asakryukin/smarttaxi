package com.example.smarttaxi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Validation extends Activity implements OnClickListener {
	private JSONObject json;
	private EditText code;
	private Button send,cancel;
	private String android_id; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validation);
		android_id = Secure.getString(this.getContentResolver(),
	            Secure.ANDROID_ID); 
		code=(EditText) findViewById(R.id.validation_code);
		send=(Button) findViewById(R.id.validation_ok);
		cancel=(Button) findViewById(R.id.validation_cancel);
		
		send.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		try {
			json=new JSONObject(getIntent().getStringExtra("json"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public class do_validation extends AsyncTask<JSONObject, String, JSONObject> {
		
		private StringBuilder builder;
		String url,url_response;
		JSONObject json;
		@Override
		protected JSONObject doInBackground(JSONObject... JS) {
			// TODO Auto-generated method stub
			
			HttpResponse response;
			builder=new StringBuilder();
			try {
				HttpClient httpclient = new DefaultHttpClient();
				
				HttpPost httppost = new HttpPost("http://smarttaxi.newton-innovations.kz/php/validation.php");
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("HTTP_JSON", JS[0].toString()));
		        Log.d("mylog", JS[0].toString());
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);
				url_response=EntityUtils.toString(response.getEntity());
				Log.d("mylog", url_response);
				json=new JSONObject(url_response);
				
				//Log.d("mylog", response.toString());
				//Log.d("mylog", response.getEntity().getContent().toString());
				
			}catch(IOException e)
			{
				Log.d("mylog", e.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return json;
		}
		
		

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.validation_cancel) {
			finish();
		} else if (id == R.id.validation_ok) {
			do_validation dv=new do_validation();
			JSONObject jo=new JSONObject();
			try {
				jo.put("user_uuid", json.getString("user_uuid"));
				jo.put("user_phone", json.getString("user_phone"));
				jo.put("sms_code", code.getText().toString());
				jo.put("user_role", json.getString("user_role"));
				dv.execute(jo).get();
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
			
		}
	}

}
