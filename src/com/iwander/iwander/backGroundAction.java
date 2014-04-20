package com.iwander.iwander;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;



public class backGroundAction extends Service implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener, LocationListener  
{
	String contact;
	LocationClient lc;
	Location l;
	String sdistance;
	double storedLatitude;
	double storedLongitude;
	float distance;
	static float  count =0;
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		lc = new LocationClient(this, this, this);
		lc.connect();
		Toast.makeText(this, "service started!!", Toast.LENGTH_SHORT).show();	
		
		
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		request.setFastestInterval(1000);
		request.setInterval(1000);
		lc.requestLocationUpdates(request,this);
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		
		SharedPreferences settings = getSharedPreferences("iWander", 0);
		 storedLatitude = Double.parseDouble(settings.getString("latitude", "0.0"));
				

				 storedLongitude = Double.parseDouble(settings.getString("longitude", "0.0"));

			
				Location oldLoc = new Location("Point A");
				oldLoc.setLatitude(storedLatitude);
				oldLoc.setLongitude(storedLongitude);
				
				Location newLoc = new Location("POINT B");
				newLoc.setLatitude(location.getLatitude());
				newLoc.setLongitude(location.getLongitude());
				
				
				
				
				 distance = oldLoc.distanceTo(newLoc);
				 sdistance = String.valueOf(distance);
			
				Toast.makeText(this, "distance = "+sdistance, Toast.LENGTH_SHORT).show();
				
		
		
				
		
		
		if( distance >  Float.parseFloat(settings.getString("radius", "0.0") )   )
		
		{
			
			if(distance> ( Float.parseFloat(settings.getString("radius", "0.0") )+count ) )
					{
				Toast.makeText(this, String.valueOf(count),Toast.LENGTH_SHORT).show();
				
				count =count+100;
				Toast.makeText(this, String.valueOf(count),Toast.LENGTH_SHORT).show();
				
				String url = "http://maps.google.com/?q="+location.getLatitude()+","+location.getLongitude();
				 settings = getSharedPreferences("iWander", 0);
				 contact = settings.getString("contact", "0");
						

						 
			    String phoneNumber = contact;
			    String message = "Alert! Patient has crossed safe zone  last location is  "+url;

			    SmsManager smsManager = SmsManager.getDefault();
			    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
			}
			
		
			Intent dialogIntent = new Intent(getBaseContext(), Alert.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplication().startActivity(dialogIntent);	
			
		}
	}
	
	

	

}