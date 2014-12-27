package com.example.smarttaxi;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Waiting_Order extends Activity implements OnClickListener{
	TextView timer;
	private String uid;
	private int flag=0;
	Check_Status check;
	private Button cancel;
	private String android_id;
	int c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waiting);
		
		cancel=(Button) findViewById(R.id.wait_cancel);
		cancel.setOnClickListener(this);
		
		uid=getIntent().getExtras().getString("id");
		check=new Check_Status(uid);
		android_id = Secure.getString(this.getContentResolver(),
	            Secure.ANDROID_ID); 
		timer=(TextView) findViewById(R.id.wait_time);
		 new CountDownTimer(30000, 1000) {

		     public void onTick(long millisUntilFinished) {
		    	 timer.setText("seconds remaining: " + millisUntilFinished / 1000);
		    	 
		     }

		     public void onFinish() {
		    	 timer.setText("done!");
		    	 Toast.makeText(getApplicationContext(), "Sorry",  Toast.LENGTH_LONG).show();
		    	 flag=1;
		    	 Cancel_Order can=new Cancel_Order(android_id);
		    	 can.execute();
		    	 finish();
		     }
		  }.start();
		 {
			 try {
				c=check.execute().get();
				Log.d("mylog", "RES:"+c);
				//wait(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.wait_cancel:
			Cancel_Order can=new Cancel_Order(android_id);
	    	 can.execute();
	    	 flag=1;
	    	 finish();
			break;
		
		}
	}
	
	public class Check_Status extends AsyncTask<Integer, Integer, Integer>{

		private String uuid;
		
		public Check_Status(String i) {
			// TODO Auto-generated constructor stub
		uuid=i;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			HttpResponse response;
			StringBuilder builder = new StringBuilder();
			String url="http://smarttaxi.newton-innovations.kz/php/orderStatus.php";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				
				HttpPost httppost = new HttpPost("http://smarttaxi.newton-innovations.kz/php/orderStatus.php");
				JSONObject json= new JSONObject();
				json.put("order_uuid", uid);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("HTTP_JSON",json.toString()));
		       //Log.d("mylog", JS[0].toString());
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
				response = httpclient.execute(httppost);
				String url_response=EntityUtils.toString(response.getEntity());
				Log.d("mylog","Wait_RESP:"+ url_response);
				JSONObject js=new JSONObject(url_response);
				
					return js.getInt("order_status");
				}
			

			catch (Exception e) {
				Log.d("mylog", e.toString());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (result!=1 && flag==0){
				try {
					Check_Status cc=new Check_Status(uuid);
					
					c=cc.execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				finish();
			}
			
		}
		
		
		
	}
	
}
