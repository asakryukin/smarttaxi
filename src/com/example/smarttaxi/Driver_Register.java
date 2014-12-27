package com.example.smarttaxi;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class Driver_Register extends Activity implements OnClickListener, OnItemSelectedListener {
	private static final int RESULT_LOAD_IMAGE = 1;
	Button btn_cancel,btn_register;
	ImageView photo;
	Spinner brand,model;
	private String android_id;
	private String encoded="";
	EditText name,surname,phone,carid;
	SQLiteDatabase myDB;
	JSONObject result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_register);
		android_id = Secure.getString(this.getContentResolver(),
	            Secure.ANDROID_ID); 
		btn_cancel=(Button) findViewById(R.id.driver_register_cancel);
		btn_cancel.setOnClickListener(this);
		btn_register=(Button) findViewById(R.id.driver_register_register);
		btn_register.setOnClickListener(this);
		photo=(ImageView) findViewById(R.id.driver_register_photo);
		photo.setOnClickListener(this);
		
		name=(EditText) findViewById(R.id.driver_register_name);
		surname=(EditText) findViewById(R.id.driver_register_surname);
		phone=(EditText) findViewById(R.id.driver_register_phone);
		carid=(EditText) findViewById(R.id.driver_register_carid);
		
		brand=(Spinner) findViewById(R.id.driver_register_brand);
		model=(Spinner) findViewById(R.id.driver_register_carmodel);
		
		String[] columns={"_id","name"};
		myDB=getApplicationContext().openOrCreateDatabase("SmartTaxi", 0, null);
		//Cursor c=myDB.query("University", columns, null, null, null, null, null);
		Cursor c=myDB.rawQuery("SELECT * FROM Brands", null);
		//Log.d(TAG, "c:"+c.getColumnCount()+" n:"+c.getCount()+"   "+c.toString());
		
		String[] from={"name"};
		int[] to={R.id.simple_spinner_item};
		
		SimpleCursorAdapter cadapter=new SimpleCursorAdapter(getApplicationContext(), R.layout.simple_spinner_item, c, from, to);
		
		brand.setAdapter(cadapter);
		
		brand.setSelection(0);
		brand.setOnItemSelectedListener(this);
		
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.driver_register_photo) {
			Intent i= new Intent(
					Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					 
					startActivityForResult(i, RESULT_LOAD_IMAGE);
		} else if (id == R.id.driver_register_cancel) {
			finish();
		} else if (id==R.id.driver_register_register){
			Register reg=new Register();
			JSONObject jo=new JSONObject();
			try {
				
				jo.put("user_uuid", android_id);
				jo.put("user_fname", name.getText().toString());
				jo.put("user_lname", surname.getText().toString());
				jo.put("user_phone", phone.getText().toString());
				jo.put("user_img", encoded);
				jo.put("user_role", "2");
				jo.put("driver_car_brand",""+brand.getSelectedItemId());
				jo.put("driver_car_model", ""+model.getSelectedItemId());
				jo.put("driver_car_color", "red");

				jo.put("driver_car_id", carid.getText().toString());
				/*jo.put("user_uuid", "abcd");
				jo.put("user_fname", "asdfasdf");
				jo.put("user_phone", "7011234567");
				jo.put("user_role", "1");*/
				result=reg.execute(jo).get();
				if (result.getInt("success")==0){
					Toast.makeText(this, result.getString("message"), Toast.LENGTH_LONG).show();
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
			Log.d("mylog", result.toString());
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		

		if (requestCode==RESULT_LOAD_IMAGE&& resultCode == -1 && null != data)
	{
		Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
       
        cursor.close();
         
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        
        Bitmap bm=BitmapFactory.decodeFile(picturePath);
        bm=scaleDownBitmap(bm, 350, this);
        //bm=Bitmap.createScaledBitmap(bm, 350,350, false);
        photo.setImageBitmap(bm);
        
        
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object   
        byte[] b = baos.toByteArray();
        encoded = Base64.encodeToString(b, Base64.DEFAULT);
	}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.d("mylog", "ONITEMSELECTED");
		
		int id1 = ((View) v.getParent()).getId();
		if (id1 == R.id.driver_register_carmodel) {
			Log.d("mylog", "MODEL");
		} else if (id1 == R.id.driver_register_brand) {
			Log.d("mylog", "BRAND");
			String[] columns={"_id","name"};
			Cursor c=myDB.rawQuery("SELECT * FROM Models WHERE brand_id=="+brand.getItemIdAtPosition(position), null);
			//Log.d(TAG, "c:"+c.getColumnCount()+" n:"+c.getCount()+"   "+c.toString());
			Log.d("mylog", ""+c.getCount());
			String[] from={"name"};
			int[] to={R.id.simple_spinner_item};
			SimpleCursorAdapter cadapter=new SimpleCursorAdapter(getApplicationContext(), R.layout.simple_spinner_item, c, from, to);
			model.setAdapter(cadapter);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

		 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        

		 int h= (int) (newHeight*densityMultiplier);
		 int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

		 photo=Bitmap.createScaledBitmap(photo, w, h, true);

		 return photo;
		 }
	
/*

public class Register extends AsyncTask<JSONObject, String, JSONObject> {
	HttpResponse response;
	private StringBuilder builder;
	String url,entity;
	
	@Override
	protected JSONObject doInBackground(JSONObject... JS) {
		// TODO Auto-generated method stub
		
		
		builder=new StringBuilder();
		try {
			HttpClient httpclient = new DefaultHttpClient();
			
			HttpPost httppost = new HttpPost("http://smarttaxi.newton-innovations.kz/php/register.php");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("HTTP_JSON", JS[0].toString()));
	       Log.d("mylog", JS[0].toString());
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpclient.execute(httppost);
			entity=EntityUtils.toString(response.getEntity());
			Log.d("mylog", entity);
			//Log.d("mylog", response.toString());
			//Log.d("mylog", response.getEntity().getContent().toString());
			
		}catch(IOException e)
		{
			Log.d("mylog", e.toString());
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try {
			Toast.makeText(getApplicationContext(), entity, Toast.LENGTH_LONG).show();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}*/
}
