package com.example.smarttaxi;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Driver_Map extends Activity implements OnClickListener {
	
	private long id;
	private SQLiteDatabase myDB= null;
	private Cursor c;
	private OverlayItem driver,from,to;
	private Overlay overlay;
	private OverlayManager mOverlayManager;
	private Double latitude,longitude;
	private MapController mMapController;
	private MapView map;
	private GeoPoint gp;
	private Button ok,no;
	private LinearLayout ll;
	private int back=1;
	private Intent service;

	private BroadcastReceiver br;
	private String android_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_map);
		android_id = Secure.getString(this.getContentResolver(),
	            Secure.ANDROID_ID); 
		//Initialization
		map=(MapView) findViewById(R.id.driver_map);
		ok=(Button) findViewById(R.id.driver_map_ok);
		no=(Button) findViewById(R.id.driver_map_no);
		no.setOnClickListener(this);
		ok.setOnClickListener(this);
		ll=(LinearLayout) findViewById(R.id.driver_map_decision);
		
		//Get information about the order
		id=getIntent().getExtras().getLong("ID");
		myDB = this.openOrCreateDatabase("SmartTaxi", MODE_PRIVATE, null);
		
		
		c=myDB.rawQuery("SELECT * FROM Orders WHERE _id="+id, null);
		c.moveToFirst();
		Log.d("mylog",""+c.getDouble(c.getColumnIndex("from_lat")));
		
		//Marking the positions
		GPSTracker tracker = new GPSTracker(this);
	    if (tracker.canGetLocation() == false) {
	        tracker.showSettingsAlert();
	    } else {
	        latitude = tracker.getLatitude();
	        longitude = tracker.getLongitude();
	    }
	    mMapController = map.getMapController();
		 
		// ���������� ����� �� �������� ����������
		mMapController.setPositionAnimationTo(new GeoPoint(latitude, longitude));
		mMapController.setZoomCurrent(17);
		
		mOverlayManager = mMapController.getOverlayManager();
		overlay = new Overlay(mMapController);
		overlay.clearOverlayItems();
		gp=new GeoPoint(latitude, longitude);
		driver = new OverlayItem(gp,getResources().getDrawable(R.drawable.ic_launcher));
		// ��������� ������ �� ����
		overlay.addOverlayItem(driver);
		
		gp=new GeoPoint(c.getDouble(c.getColumnIndex("from_lat")), c.getDouble(c.getColumnIndex("from_lon")));
		from=new OverlayItem(gp,getResources().getDrawable(R.drawable.ic_launcher));
		// ��������� ������ �� ����
		overlay.addOverlayItem(from);
		
		gp=new GeoPoint(c.getDouble(c.getColumnIndex("destination_lat")), c.getDouble(c.getColumnIndex("destination_lon")));
		to=new OverlayItem(gp,getResources().getDrawable(R.drawable.ic_launcher));
		// ��������� ������ �� ����
		overlay.addOverlayItem(to);
		
		// ��������� ���� �� �����
		mOverlayManager.addOverlay(overlay);
		
		
		
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("mylog", "onSTART!");
		br=new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				
				if (intent.getExtras()!=null){
					if (intent.getIntExtra("status", 0)>0){
				Log.d("mylog", "RECEIved!");
				
				if (intent.getIntExtra("status", 0)<2){
					//startService(service);
					overlay.removeOverlayItem(driver);
					mOverlayManager.removeOverlay(overlay);
					gp=new GeoPoint(intent.getDoubleExtra("latitude", 0), intent.getDoubleExtra("longitude", 0));
					driver = new OverlayItem(gp,getResources().getDrawable(R.drawable.ic_launcher));
					// ��������� ������ �� ����
					overlay.addOverlayItem(driver);
					mOverlayManager.addOverlay(overlay);
				}else {
					finish();
				}
				}
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
	      intentFilter.addAction(Driver_Position_Service.MY_ACTION);
		registerReceiver(br, intentFilter);
	      
	}
	
	@Override
	protected void onStop() {
	 // TODO Auto-generated method stub
	 unregisterReceiver(br);
	 super.onStop();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.driver_map_ok:
			JSONObject js=new JSONObject();
			JSONObject res;
			Select_Order so=new Select_Order();
			try {
				js.put("driver_uuid", android_id);
				js.put("order_uuid", id);
				res=so.execute(js).get();
				if (res.getInt("success")==1){
					ll.setVisibility(ll.GONE);
					back=0;
					service=new Intent(getApplicationContext(), Driver_Position_Service.class);
					service.putExtra("id", id);
					startService(service);
					
				}
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
			
			
			
			break;
			
		case R.id.driver_map_no:
			finish();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		
		if (back==1){
			finish();
		}
		else {
			
		}
	}

}
