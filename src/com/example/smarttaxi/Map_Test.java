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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Map_Test extends FragmentActivity implements OnClickListener, OnTouchListener {
		EditText search;
		Button btn;
		MapView map;
		private double latitude,longitude;
		MapController mMapController;
		LinearLayout ll;
	  @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_test);
			    
		ll=(LinearLayout) findViewById(R.id.map_main_layout);
		search=(EditText) findViewById(R.id.map_test_adr);    
		btn=(Button) findViewById(R.id.map_test_ok);
		btn.setOnClickListener(this);
		
		map=(MapView) findViewById(R.id.map);
		GPSTracker tracker = new GPSTracker(this);
	    if (tracker.canGetLocation() == false) {
	        tracker.showSettingsAlert();
	    } else {
	        latitude = tracker.getLatitude();
	        longitude = tracker.getLongitude();
	    }
		mMapController = map.getMapController();
		 
		// Перемещаем карту на заданные координаты
		mMapController.setPositionAnimationTo(new GeoPoint(latitude, longitude));
		mMapController.setZoomCurrent(17);
		ll.setClickable(true);
		ll.setOnTouchListener(this);
		
		
		    
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
				mMapController.setPositionAnimationTo(new GeoPoint(Double.parseDouble(separated[1]), Double.parseDouble(separated[0])));
				mMapController.setZoomCurrent(17);
				
				// Получаем объект OverlayManager
				OverlayManager mOverlayManager = mMapController.getOverlayManager();
				//Добавляем новый слой
				
				//Создаем новый слой
				Overlay overlay = new Overlay(mMapController);
				// Создаем объект слоя
				OverlayItem yandex = new OverlayItem(new GeoPoint(Double.parseDouble(separated[1]), Double.parseDouble(separated[0])),getResources().getDrawable(R.drawable.ic_launcher));
				// Добавляем объект на слой
				overlay.addOverlayItem(yandex);
				// Добавляем слой на карту
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
		
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int)event.getX();
	    int y = (int)event.getY();
	    Log.d("mylog", "TOUCH");
	    ScreenPoint leftTop = new ScreenPoint(x, y);
	    GeoPoint leftTopGP = mMapController.getGeoPoint(leftTop);
	    Log.d("mylog", "TOUCH:"+x+" "+y);
	    Log.d("mylog", ""+leftTopGP.getLat()+" "+leftTopGP.getLon());
	 // Получаем объект OverlayManager
		OverlayManager mOverlayManager = mMapController.getOverlayManager();
		//Добавляем новый слой
		
		//Создаем новый слой
		Overlay overlay = new Overlay(mMapController);
		// Создаем объект слоя
		OverlayItem yandex = new OverlayItem(leftTopGP,getResources().getDrawable(R.drawable.ic_launcher));
		// Добавляем объект на слой
		overlay.addOverlayItem(yandex);
		// Добавляем слой на карту
		mOverlayManager.addOverlay(overlay);
		return false;
	}
	  
	  
}
