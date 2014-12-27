package com.example.smarttaxi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;


public class Fill_models extends AsyncTask<Void, String[], ArrayList<Map<Integer,String>>>{
	private SQLiteDatabase myDB= null;
	public Fill_models(Context cnt) {
		// TODO Auto-generated constructor stub
		myDB = cnt.openOrCreateDatabase("SmartTaxi", 0, null);
	}
	  	@Override
	  	protected ArrayList<Map<Integer, String>> doInBackground(Void... arg0) {
	  		// TODO Auto-generated method stub
	  		
	  		HttpResponse response;
			StringBuilder builder = new StringBuilder();
			String url="http://smarttaxi.newton-innovations.kz/cars/car_models.json";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				
				HttpGet httpget = new HttpGet(url);
				HttpParams httpParameters = new BasicHttpParams();
				//HttpConnectionParams.setConnectionTimeout(httpParameters, 50000);
				response = httpclient.execute(httpget);
				Log.d("mylog", "resp");

				// Get hold of the response entity
				HttpEntity entity = response.getEntity();
				Log.d("mylog", "enti");

				if (entity != null) {
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						InputStream content = entity.getContent();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(content));
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					}

					JSONArray jsonArray = new JSONArray(builder.toString());
					//JSONArray jsonArray = json.getJSONArray("data");
					//Log.d("mylog", json.toString());
					Log.d("mylog", jsonArray.toString());
					for(int i=0;i<jsonArray.length();i++)
					{
					
					 Log.d("mylog", jsonArray.getJSONObject(i).getString("Models_Name"));
					 myDB.execSQL("INSERT INTO Models"
						     + " (_id, name,brand_id)"
						     + " VALUES ("+jsonArray.getJSONObject(i).getInt("Model_ID")+",'"
						     +jsonArray.getJSONObject(i).getString("Models_Name")+"',"
						     +jsonArray.getJSONObject(i).getInt("Brands_ID")+");");
					
					}
				}
			}

			catch (Exception e) {
				Log.d("mylog", e.toString());
			}
	  		return null;
	  	}
	  	  
	    

}
