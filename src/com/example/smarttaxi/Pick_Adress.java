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
import ru.yandex.yandexmapkit.utils.ScreenPoint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Pick_Adress extends Activity implements OnClickListener, OnTouchListener {
	EditText search;
	Button btn,btn_send;
	MapView map;
	private double latitude,longitude;
	MapController mMapController;
	private Display display;
	private Point size;
	private int width, height;
	LinearLayout ll,llm;
	private OverlayItem yandex;
	private Overlay overlay;
	private OverlayManager mOverlayManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_test);
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		search=(EditText) findViewById(R.id.map_test_adr);    
		btn=(Button) findViewById(R.id.map_test_ok);
		btn.setOnClickListener(this);
		btn_send=(Button) findViewById(R.id.map_main_send);
		btn_send.setOnClickListener(this);
		
		
		map=(MapView) findViewById(R.id.map);
		
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
		
		btn.setOnClickListener(this);
		ll=(LinearLayout) findViewById(R.id.map_main_layout);
		llm=(LinearLayout) findViewById(R.id.map_main_touch);
		
		mOverlayManager = mMapController.getOverlayManager();
		overlay = new Overlay(mMapController);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.map_test_ok:
			Get_Yandex_Coordinates res=new Get_Yandex_Coordinates();
			JSONObject js;
			try {
				js=res.execute(search.getText().toString()).get();
				String pos;
				pos=js.getJSONObject("response").getJSONObject("GeoObjectCollection").getJSONArray("featureMember").getJSONObject(0).getJSONObject("GeoObject").getJSONObject("Point").getString("pos");
				Log.d("mylog", pos);
				String[] separated = pos.split(" ");
				latitude=Double.parseDouble(separated[1]);
				longitude=Double.parseDouble(separated[0]);
				Log.d("mylog", "lat:"+latitude+"   "+longitude);
				mMapController.setPositionAnimationTo(new GeoPoint(Double.parseDouble(separated[1]), Double.parseDouble(separated[0])));
				mMapController.setZoomCurrent(17);
				
				overlay.clearOverlayItems();
				// ������� ������ ����
				yandex = new OverlayItem(new GeoPoint(Double.parseDouble(separated[1]), Double.parseDouble(separated[0])),getResources().getDrawable(R.drawable.ic_launcher));
				// ��������� ������ �� ����
				overlay.addOverlayItem(yandex);
				// ��������� ���� �� �����
				mOverlayManager.addOverlay(overlay);

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
		case R.id.map_main_send:
			Intent intent = new Intent();
		    intent.putExtra("latitude", latitude);
		    intent.putExtra("longitude", longitude);
		    setResult(RESULT_OK, intent);
		    finish();
			break;
		}
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("mylog", "Touched!");
		return false;
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
	    // TODO Auto-generated method stub
	    super.dispatchTouchEvent(ev);
	    Log.d("mylog", "X:"+ev.getX()+" Y:"+ev.getY()+" LL:"+2*ll.getHeight());
	    
	    if (ev.getY()>2.2*ll.getHeight()&&ev.getY()<(height-ll.getHeight())){
	    	Log.d("mylog","IT WAS TOUCHED!");
	    	ScreenPoint leftTop = new ScreenPoint(width/2, height/2-height+map.getHeight()+ll.getHeight());
		    GeoPoint leftTopGP = mMapController.getGeoPoint(leftTop);
		   //Log.d("mylog", "TOUCH:"+x+" "+y);
		    latitude=leftTopGP.getLat();
		    longitude=leftTopGP.getLon();

			Log.d("mylog", "lat:"+latitude+"   "+longitude);
		    Log.d("mylog", ""+leftTopGP.getLat()+" "+leftTopGP.getLon());
		 // �������� ������ OverlayManager
			mOverlayManager = mMapController.getOverlayManager();
			//��������� ����� ����
			
			//������� ����� ����
			// ������� ������ ����

			overlay.clearOverlayItems();
			yandex = new OverlayItem(leftTopGP,getResources().getDrawable(R.drawable.ic_launcher));
			// ��������� ������ �� ����
			overlay.addOverlayItem(yandex);
			// ��������� ���� �� �����
			mOverlayManager.addOverlay(overlay);
	    }
	    
	    return true;
	}

}
