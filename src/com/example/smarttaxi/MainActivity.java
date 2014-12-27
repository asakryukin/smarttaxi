package com.example.smarttaxi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


public class MainActivity extends Activity {
	private SQLiteDatabase myDB= null;
	private Cursor c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		/*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		//create a database
				myDB = this.openOrCreateDatabase("SmartTaxi", MODE_PRIVATE, null);
				myDB.execSQL("create table if not exists Active ("
				          + "_id integer primary key,"
				          + "user_uuid text,"
				          + "user_phone text,"
				          + "user_fname text,"
				          + "user_lname text,"
				          + "driver_img text,"
				          + "driver_car_id text,"
				          + "driver_car_brand text,"
				          + "driver_car_model text,"
				          + "driver_car_color text,"
				          + "user_role integer"
				          + ");");
				/*
				myDB.execSQL("create table if not exists Active ("
				          + "_id integer primary key," 
				          + "type integer,"
				          + "tel text,"
				          + "name string," 
				          + "android_id string"
				          + ");");
				*/
				myDB.execSQL("create table if not exists Brands ("
				          + "_id integer primary key,"
				          + "name string"
				          + ");");
				myDB.execSQL("create table if not exists Models ("
				          + "_id integer primary key,"
				          + "name string,"
				          + "brand_id integer"
				          + ");");
				Cursor cb=myDB.rawQuery("SELECT * FROM Brands", null);
				
				if (cb.getCount()<=0)
				{
					Fill_brands fb=new Fill_brands(this);
					fb.execute();
				}
				
				Cursor cm=myDB.rawQuery("SELECT * FROM Models", null);
				
				if (cm.getCount()<=0)
				{
					Fill_models fm=new Fill_models(this);
					fm.execute();
				}
				
				Cursor c=myDB.rawQuery("SELECT * FROM Active", null);
				
				if (c.getCount()>0)
				{
					int type;
					c.moveToFirst();
					type=c.getInt(c.getColumnIndex("user_role"));
					if (type==1){
						Intent intent=new Intent(getApplicationContext(), Client_Main.class);
						startActivity(intent);
					}
					else 
					{
						Intent intent=new Intent(getApplicationContext(), Driver_Main.class);
						startActivity(intent);
					}
				}
				else{
					
					Intent intent=new Intent(getApplicationContext(), start_menu.class);
					startActivity(intent);
				}
				finish();
	}

	
}
