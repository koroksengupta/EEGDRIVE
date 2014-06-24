package com.android.proxymityalert;

import com.android.proxymityalert.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProximityAlertActivity extends Activity{
	private static final String TAG = "ProximityAlert";
	private static final long POINT_RADIUS = 100; // in Meters
	private static final long PROX_ALERT_EXPIRATION = -1; // It will never
	private static final String PROX_ALERT_INTENT = "com.android.ProximityAlert";
	private LocationManager mLocationManager;
	private Button mStartDrive;
	private Button mEndDrive;
	private TextView mPressStart;	
	private TextView mStatus;
    private int mRequestCode = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proxymity);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mPressStart = (TextView) findViewById(R.id.press_start);
		mStatus = (TextView) findViewById(R.id.status_text);
		initButton();
	}



	private void initButton() {
		mStartDrive = (Button) findViewById(R.id.car_drive_start);
		mStartDrive.setOnClickListener(new OnClickListener(){
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
		});
		
		mEndDrive = (Button) findViewById(R.id.car_drive_stop);
		mEndDrive.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				removerAlert();
				stopEegDataService();
				mPressStart.setVisibility(View.VISIBLE);
				mStartDrive.setVisibility(View.VISIBLE);
	            mEndDrive.setVisibility(View.GONE);
				mStatus.setVisibility(View.GONE);
			}	
		});
	}

	private void addProximityAlert(double latitude, double longitude) {
		Intent intent = new Intent(PROX_ALERT_INTENT);
		intent.putExtra(EegUtil.EVENT_ID_INTENT_EXTRA, mRequestCode + 1);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), mRequestCode,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Log.d(EegUtil.TAG, "Request Code for each: "+mRequestCode);
		mLocationManager.addProximityAlert(latitude, // the latitude of the
													// central point of the
													// alert region
				longitude, // the longitude of the central point of the
							// alert region
				POINT_RADIUS, // the radius of the central point of the
								// alert region, in meters
				PROX_ALERT_EXPIRATION, // time for this proximity alert, in
										// milliseconds, or -1 to indicate
										// no expiration
				proximityIntent // will be used to generate an Intent to
								// fire when entry to or exit from the alert
								// region is detected
				);

		IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
		registerReceiver(new ProximityIntentReceiver(), filter);
		Toast.makeText(getApplicationContext(), "Alert Added",
				Toast.LENGTH_SHORT).show();
		mRequestCode++;
	}
	
	private void removerAlert(){
		Intent intent = new Intent(PROX_ALERT_INTENT);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		mLocationManager.removeProximityAlert(proximityIntent);
	}
	
	private void stopEegDataService(){
		Intent intent = new Intent(this, GetEegData.class);
		stopService(intent);		
	}


}
