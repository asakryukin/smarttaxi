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

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

public class Driver_Position_Service extends Service {
	private Double latitude,longitude;
	private GetStatus gs;
	private Position_Request pr;
	final static String MY_ACTION = "POSITIONING";
	private long order_id;
	private int status;
	private GPSTracker tracker;
	private Do_Service_Staff dsf;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		tracker = new GPSTracker(this);
		status=0;
		order_id=intent.getExtras().getLong("id");
		gs=new GetStatus();
		dsf=new Do_Service_Staff();
		dsf.execute(0);
		
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
	
	private class Do_Service_Staff extends AsyncTask<Integer, JSONObject, JSONObject>{
		Time now;
		long curt=0;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			now = new Time();
			now.setToNow();
			curt=now.toMillis(false);
			gs=new GetStatus();
			JSONObject js=new JSONObject();
			try {
				js.put("order_uuid", order_id);
				status=gs.execute(js).get().getInt("order_status");
				Log.d("mylog", "PRE_STATUS:"+status);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("mylog", "PRE_STATUS2:"+status);
			if (tracker.canGetLocation() == false) {
		        tracker.showSettingsAlert();
		    } else {
		        latitude = tracker.getLatitude();
		        longitude = tracker.getLongitude();
		    }
		}
		@Override
		protected JSONObject doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			
			
			
			
			//while (status<2)
			{
			
		    
		    Log.d("mylog", "SerLat:"+latitude+" :"+longitude);
			
			
			
			}
			/*
			try {
			//	Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			while (now.toMillis(false)-curt<3000){
				now.setToNow();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Intent broad=new Intent();
			broad.setAction(MY_ACTION);
			broad.putExtra("status", status);
			broad.putExtra("latitude", latitude);
			broad.putExtra("longitude", longitude);
			sendBroadcast(broad);
			
			JSONObject position=new JSONObject();
			position=new JSONObject();
			
			try {
				position.put("driver_uuid", order_id);
				position.put("driver_lat", latitude.toString());
				position.put("driver_lon", longitude.toString());
				pr= new Position_Request(getResources().getString(R.string.Set_Position_Url));
				pr.execute(position).get();
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
			
			Log.d("mylog", "POSTEXEC");
			if (status<2){
				dsf=new Do_Service_Staff();
				dsf.execute(0);
			}
		}
		
	}
	
	private class GetStatus extends AsyncTask<JSONObject, Integer, JSONObject>{
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
				
				HttpPost httppost = new HttpPost("http://smarttaxi.newton-innovations.kz/php/orderStatus.php");
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("HTTP_JSON", JS[0].toString()));
		        Log.d("mylog", JS[0].toString());
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);
				url_response=EntityUtils.toString(response.getEntity());
				Log.d("mylog", "ORDER_STATUS:"+url_response);
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
	

}
