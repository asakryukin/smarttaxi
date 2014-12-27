package com.example.smarttaxi;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class Client_Main extends Activity implements OnClickListener {
		
		private final int PICK_FROM=100;
		private final int PICK_TO=200;
		private Button btn_from,btn_to,btn_go;
		private EditText price;
		private String android_id;
		private double from_latitude,from_longitude,to_latitude,to_longitude;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		setContentView(R.layout.client_main);
		android_id = Secure.getString(this.getContentResolver(),
	            Secure.ANDROID_ID); 
		btn_from=(Button) findViewById(R.id.client_main_from);
		btn_from.setOnClickListener(this);
		btn_to=(Button) findViewById(R.id.client_main_to);
		btn_to.setOnClickListener(this);
		btn_go=(Button) findViewById(R.id.client_main_go);
		btn_go.setOnClickListener(this);
		price=(EditText) findViewById(R.id.client_main_price);
		
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent;
			switch(v.getId()){
			case R.id.client_main_from:
				intent=new Intent(this, Pick_Adress.class);
				startActivityForResult(intent, PICK_FROM);
				break;
				
			case R.id.client_main_to:
				intent=new Intent(this, Pick_Adress.class);
				startActivityForResult(intent, PICK_TO);
				break;
			case R.id.client_main_go:
				Make_Order mo=new Make_Order();
				JSONObject jo=new JSONObject();
				try {
					jo.put("lat_from",""+from_latitude);
					jo.put("lon_from",""+from_longitude);
					jo.put("lat_destination",""+to_latitude);
					jo.put("lon_destination",""+to_longitude);
					jo.put("order_price",price.getText().toString());
					jo.put("user_uuid",android_id);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					intent=new Intent(getApplicationContext(), Waiting_Order.class); 
					intent.putExtra("id",mo.execute(jo).get().getString("order_uuid"));
					startActivity(intent);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				break;
			}
		}
		  @Override
		  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   // if (data == null) {return;}
			  Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);
			    // если пришло ОК
		    if (requestCode==PICK_FROM){
		    	from_latitude=data.getDoubleExtra("latitude",0);
		    	from_longitude=data.getDoubleExtra("longitude",0);
		    	Log.d("mylog", "FROM:"+from_latitude+"    "+from_longitude);
		    }else if (requestCode==PICK_TO){
		    	to_latitude=data.getDoubleExtra("latitude",0);
		    	to_longitude=data.getDoubleExtra("longitude",0);
		    	Log.d("mylog", "TO:"+to_latitude+"    "+to_longitude);
		    }
		  }
}
