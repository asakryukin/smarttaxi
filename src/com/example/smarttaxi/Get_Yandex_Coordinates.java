package com.example.smarttaxi;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class Get_Yandex_Coordinates extends AsyncTask<String, String, JSONObject>{

 
	HttpResponse response;
	private StringBuilder builder;
	String url,url_response;
	JSONObject json;
	
	@Override
	protected JSONObject doInBackground(String... JS) {
		// TODO Auto-generated method stub
		
		
		builder=new StringBuilder();
		try {
			HttpClient httpclient = new DefaultHttpClient();
			JS[0]=JS[0].replace(" ", "+");
			HttpGet httpget = new HttpGet("http://geocode-maps.yandex.ru/1.x/?format=json&geocode=Астана+"+JS[0]);
			

	       Log.d("mylog", JS[0].toString());
	        
			response = httpclient.execute(httpget);
			url_response=EntityUtils.toString(response.getEntity());
			json=new JSONObject(url_response);
			Log.d("mylog", url_response);
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
	
	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
	}
	


}
