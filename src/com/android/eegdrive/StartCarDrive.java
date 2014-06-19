package com.android.eegdrive;

import com.android.eegdrive.util.EegUtil;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartCarDrive extends Activity {
	
	private LocationManager mLocationManager;
	
	private Button mStartDrive;
	
	private Button mEndDrive;
	
	private TextView mPressStart;
	
	private TextView mStatus;
	
	private ProximityIntentReceiver mReceiver1 = null;
	
	private ProximityIntentReceiver mReceiver2 = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_car);
		mPressStart = (TextView) findViewById(R.id.press_start);
		mStartDrive = (Button) findViewById(R.id.car_drive_start);
		mStartDrive.setOnClickListener(mStartDriveListener);
		mEndDrive = (Button) findViewById(R.id.car_drive_stop);
		mEndDrive.setOnClickListener(mEndDriveListener);
		mStatus = (TextView) findViewById(R.id.status_text);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}
	
	private View.OnClickListener mStartDriveListener = new View.OnClickListener() {	
		@Override
		public void onClick(View v) {
			mPressStart.setVisibility(View.GONE);
			mStartDrive.setVisibility(View.GONE);
            mEndDrive.setVisibility(View.VISIBLE);
			mStatus.setVisibility(View.VISIBLE);
			mStatus.setText(EegUtil.DRIVE_START_STATUS);
			addProximityAlert(EegUtil.LATITUDE_1, EegUtil.LONGITUDE_1);
			addProximityAlert(EegUtil.LATITUDE_2, EegUtil.LONGITUDE_2);
			
		}
	};
	
	private View.OnClickListener mEndDriveListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			removerAlert();
			stopEegDataService();
			mPressStart.setVisibility(View.VISIBLE);
			mStartDrive.setVisibility(View.VISIBLE);
            mEndDrive.setVisibility(View.GONE);
			mStatus.setVisibility(View.GONE);			
		}
	};
	

	private void addProximityAlert(double latitude, double longitude){
		Intent intent = new Intent(EegUtil.PROXIMITY_ALERT_INTENT);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		mLocationManager.addProximityAlert(latitude, // the latitude of the
				// central point of the
				// alert region
				longitude, // the longitude of the central point of the
				// alert region
				EegUtil.POINT_RADIUS, // the radius of the central point of the
				// alert region, in meters
				EegUtil.PROX_ALERT_EXPIRATION, // time for this proximity alert, in
				// milliseconds, or -1 to indicate
				// no expiration
				proximityIntent // will be used to generate an Intent to
				// fire when entry to or exit from the alert
				// region is detected
				);

		IntentFilter filter = new IntentFilter(EegUtil.PROXIMITY_ALERT_INTENT);
		if(EegUtil.LATITUDE_1 == latitude){
			mReceiver1 = new ProximityIntentReceiver();
			registerReceiver(mReceiver1, filter);
		}else if(EegUtil.LATITUDE_2 == latitude){
			mReceiver2 = new ProximityIntentReceiver();
			registerReceiver(mReceiver2, filter);
		}
		registerReceiver(new ProximityIntentReceiver(), filter);
		Toast.makeText(getApplicationContext(), "Alert Added",
				Toast.LENGTH_SHORT).show();
	}
	
	private void removerAlert(){
		Intent intent = new Intent(EegUtil.PROXIMITY_ALERT_INTENT);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		mLocationManager.removeProximityAlert(proximityIntent);
	}
	
	private void stopEegDataService(){
		Intent intent = new Intent(this, GetEegData.class);
		stopService(intent);		
	}
	

}
