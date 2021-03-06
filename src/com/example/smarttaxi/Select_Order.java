package com.example.smarttaxi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import android.os.AsyncTask;
import android.util.Log;

public class Select_Order extends AsyncTask<JSONObject, String, JSONObject>{
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
			
			HttpPost httppost = new HttpPost("http://smarttaxi.newton-innovations.kz/php/orderSelect.php");
			
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
			return json;
		}catch(IOException e)
		{
			Log.d("mylog", e.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
