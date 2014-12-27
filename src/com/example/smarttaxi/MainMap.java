package com.example.smarttaxi;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;


public class MainMap extends Activity {
	
	private GPSTracker gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_map);
		
		Location mL;
		LocationManager lm;
		
		lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		TextView txt;
		txt=(TextView) findViewById(R.id.mainmap_txt2);
		
		gps= new GPSTracker(this);
		txt.setText(""+gps.getLatitude());
		
		
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		gps.stopUsingGPS();
		
	}

}
