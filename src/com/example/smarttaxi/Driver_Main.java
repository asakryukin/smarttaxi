package com.example.smarttaxi;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class Driver_Main extends Activity implements OnItemClickListener {
		private double latitude,longitude;
		private ListView list;
		private JSONArray res;
		private SQLiteDatabase myDB= null;
		private Cursor c;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.driver_main);
			myDB = this.openOrCreateDatabase("SmartTaxi", MODE_PRIVATE, null);
			//myDB.execSQL("DROP Orders");
			myDB.execSQL("create table if not exists Orders ("
			          + "_id integer primary key,"
			          + "order_price text,"
			          + "from_lat text,"
			          + "from_lon text,"
			          + "destination_lat text,"
			          + "destination_lon text,"
			          + "client_uuid text,"
			          + "distance text"
			          + ");");
			c=myDB.rawQuery("SELECT * FROM Orders", null);
			
			if (c.getCount()>0){
				myDB.execSQL("DELETE FROM Orders");
			}
			//
			list=(ListView) findViewById(R.id.driver_main_list);
			list.setOnItemClickListener(this);
			GPSTracker tracker = new GPSTracker(this);
		    if (tracker.canGetLocation() == false) {
		        tracker.showSettingsAlert();
		    } else {
		        latitude = tracker.getLatitude();
		        longitude = tracker.getLongitude();
		    }
		    
		    Get_Orders go=new Get_Orders();
		    JSONObject jo= new JSONObject();
		    res=new JSONArray();
		    try {
				jo.put("driver_longitude", longitude);
				jo.put("driver_latitude", latitude);
			    jo.put("radius", 1000);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    try {
				res=go.execute(jo).get();
				Log.d("mylog", res.toString());
				JSONObject t;
				for(int i=0;i<res.length();i++){
					t=res.getJSONObject(i);
					myDB.execSQL("INSERT INTO Orders"
						     + " (_id,order_price,from_lat,from_lon,destination_lat,destination_lon,client_uuid,distance)"
						     + " VALUES ("+t.getString("order_uuid")+",'"
						     +t.getString("order_price")+"','"+t.getString("from_lat")+"','"+
						     t.getString("from_lon")+"','"+t.getString("destination_lat")+"','"+
						     t.getString("destination_lon")+"','"+t.getString("client_uuid")+"','"+
						     t.getString("Distance")+"')");
					
				}
			
			c=myDB.rawQuery("SELECT * FROM Orders ORDER BY distance", null);
			String[] from={"distance","order_price"};
			int[] to={R.id.list_item_dist,R.id.list_item_price};
			SimpleCursorAdapter adapter=new SimpleCursorAdapter(getApplicationContext(), R.layout.layout_item, c, from, to);	
			list.setAdapter(adapter);
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
			
		}
		@Override
		public void onItemClick(AdapterView<?> adapter, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(getApplicationContext(), Driver_Map.class);
			intent.putExtra("ID", id);
			startActivity(intent);
		}
}
