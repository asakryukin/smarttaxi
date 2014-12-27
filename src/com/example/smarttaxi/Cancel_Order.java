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

import android.os.AsyncTask;
import android.util.Log;

import com.example.smarttaxi.Waiting_Order.Check_Status;


public class Cancel_Order extends AsyncTask<Integer, Integer, Integer>{

	private String uuid;
	
	public Cancel_Order(String i) {
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
			
			HttpPost httppost = new HttpPost("http://smarttaxi.newton-innovations.kz/php/orderCancel.php");
			JSONObject json= new JSONObject();
			json.put("user_uuid", uuid);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("HTTP_JSON",json.toString()));
	       //Log.d("mylog", JS[0].toString());
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			response = httpclient.execute(httppost);
			String url_response=EntityUtils.toString(response.getEntity());
			JSONObject js=new JSONObject(url_response);
				
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

		
	}
	
	
	
}