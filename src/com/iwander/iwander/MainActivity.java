package com.iwander.iwander;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends FragmentActivity 
implements GooglePlayServicesClient.ConnectionCallbacks , 
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener
{
	final Context context = this;
	
	LocationClient mLocationClient;
	Button btnSave;
	CheckBox cbLocation;
	
	//Button btnContact;
	
	EditText etRadius;
	EditText etContact;
	EditText radius;
	
	//Button getDistance; // to show distance in next GetDistance page
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		startService(new Intent(getBaseContext(), backGroundAction.class));
	}
	

	double longitude; // store longitude here
	double latitude; // store latitude here

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        stopService(new Intent(getBaseContext(), backGroundAction.class));
        
		/*SharedPreferences settings = getSharedPreferences("iWander",0);
		
		Editor editor = settings.edit();
        */
        mLocationClient = new LocationClient(MainActivity.this , this , this);
        btnSave=(Button) findViewById(R.id.btnSave);
        cbLocation=(CheckBox) findViewById(R.id.cbLocation);
        etRadius = (EditText) findViewById(R.id.etRadius);
        etContact = (EditText) findViewById(R.id.etContact);
        
        
       // getDistance = (Button) findViewById(R.id.getDistance);
       
        
       ShowDefaultData();
        btnSave.setOnClickListener(new OnClickListener() {
        	
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				boolean checked;
				checked=cbLocation.isChecked();
				
				if(checked)
				{
					// Toast.makeText(MainActivity.this,"You checked", Toast.LENGTH_SHORT).show();
					getLocation();
					//storeLongLat(String.valueOf(longitude), String.valueOf(latitude));
					
				}
				
				String radius=  etRadius.getText().toString();
				String contact= etContact.getText().toString();
				
				storedata(radius , contact);
				storeLongLat(String.valueOf(longitude),String.valueOf(latitude));
				
				Toast.makeText(MainActivity.this, "Data Saved Successfully!!", Toast.LENGTH_LONG).show();
				Toast.makeText(MainActivity.this, "EXIT APP NOW \n TO WORK IN BACKGROUND ", Toast.LENGTH_LONG).show();
				
			}

			
		});
        
        
      /* getDistance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			
				//Intent intent = new Intent(context , GetDistance.class);
				//startActivity(intent);
			}
		});
		
		
        */
        
        
        
       //
        

            }


   protected void getLocation() {
		// TODO Auto-generated method stub
	   

  	 
	        
			
 
	  if(mLocationClient.isConnected())
	   {
  	 	
		   Location currentLocation = mLocationClient.getLastLocation();
			
		  /* latitude = currentLocation.getLatitude();
		   longitude = currentLocation.getLatitude();
		   
		   Toast.makeText(this, Double.toString(latitude)+" "+Double.toString(longitude), Toast.LENGTH_LONG).show();
		   
		  */ 
		   
	 }
	 
	   else
	   {
		  
		   Toast.makeText(this, "Not connected", Toast.LENGTH_LONG).show();
	   }
   }

@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


@Override
public void onConnectionFailed(ConnectionResult result) {
	// TODO Auto-generated method stub
	
}


@Override
public void onConnected(Bundle connectionHint) {
	// TODO Auto-generated method stub
	//Toast.makeText(this, "Onconnected", Toast.LENGTH_SHORT).show();
	LocationRequest request = LocationRequest.create();
	request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	request.setFastestInterval(4000);
	request.setInterval(5000);
	mLocationClient.requestLocationUpdates(request,this);
}


@Override
public void onDisconnected() {
	// TODO Auto-generated method stub
	
}

@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	
	
	mLocationClient.connect();
	//Toast.makeText(this, " Got connected", Toast.LENGTH_SHORT).show();

}



public void storeLongLat(String longitude , String latitude)
{
	SharedPreferences settings = getSharedPreferences("iWander",0);
	
	Editor editor = settings.edit();
    
	
	editor.putString("longitude", longitude);
	editor.putString("latitude", latitude);
	
	editor.commit();
}

public void storedata(String radius , String contact)
{
	SharedPreferences settings = getSharedPreferences("iWander",0);
	
	Editor editor = settings.edit();
    
	editor.putString("radius", radius);
	editor.putString("contact", contact);
	editor.commit();
}

public void ShowDefaultData()
{
	SharedPreferences settings = getSharedPreferences("iWander",0);
	
	longitude = Double.parseDouble(settings.getString("longitude", "0.0"));
	latitude = Double.parseDouble(settings.getString("latitude", "0.0"));
	
    
	
	etRadius.setText(settings.getString("radius", "0.0"));
	etContact.setText(settings.getString("contact" , "0"));
	
	
	
	
}

public double getLatitude()
{
	return latitude;
}

public double getLongitude()
{
	return longitude;
}


@Override
public void onLocationChanged(Location location) {
	// TODO Auto-generated method stub
	
	
	longitude = location.getLongitude();
	latitude = location.getLatitude();
	
}




    
}