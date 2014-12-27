package com.example.smarttaxi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class start_menu extends Activity implements OnClickListener {
	
	Button btn_client,btn_driver,btn_map;
	private SQLiteDatabase myDB= null;
	private Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.start_menu);
		
		btn_client=(Button) findViewById(R.id.start_menu_passenger);
		btn_driver=(Button) findViewById(R.id.start_menu_taxi);
		btn_map=(Button) findViewById(R.id.start_menu_map);
		
		btn_client.setOnClickListener(this);
		btn_driver.setOnClickListener(this);
		btn_map.setOnClickListener(this);
		
		myDB = this.openOrCreateDatabase("SmartTaxi", MODE_PRIVATE, null);

		startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		 
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.start_menu_taxi) {
			/*Cursor c=myDB.rawQuery("SELECT * FROM Profile WHERE type=1", null);
			Log.d("mylog", ""+c.getCount());*/
			//if (c.getCount()<=0)
			{
				Intent intent=new Intent(this, Driver_Choice.class);
				startActivity(intent);
				
			}
		} else if (id == R.id.start_menu_passenger) {
			TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		    String mPhoneNumber = tMgr.getLine1Number();
		    
		    if (mPhoneNumber!=null){
		    Log.d("mylog", tMgr.getSimSerialNumber());
		    }
		    else {
		    	
		    	Log.d("mylog", "null");
		    }
			Intent intent=new Intent(this, Client_Choice.class);
			startActivity(intent);
		}
		else if (id == R.id.start_menu_map)
		{
			Intent intent=new Intent(this, Map_Test.class);
			startActivity(intent);
			
		}
	}
	
}
