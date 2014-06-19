package com.android.eegdrive;

import java.util.Set;

import com.android.eegdrive.util.EegUtil;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EegConnectionStart extends Activity {

	private EditText mName;
	
	private Button mStartService;
	
	private static final int REQUEST_ENABLE_BT = 101;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mName = (EditText) findViewById(R.id.name);
		checkBtAvailability();
		mStartService = (Button) findViewById(R.id.button1);
		mStartService.setOnClickListener(mStartServiceListener);	
	}
	
	private View.OnClickListener mStartServiceListener = new View.OnClickListener() {	
		@Override
		public void onClick(View arg0) {
			startEegService();	
		}
	};


	private void startEegService() {
		Intent serviceIntent = new Intent(this, GetEegData.class);
		String driverName = mName.getText().toString();
		if (!driverName.isEmpty()) {
			serviceIntent.putExtra(EegUtil.DRIVER_NAME, driverName);
		}else{
			serviceIntent.putExtra(EegUtil.DRIVER_NAME, EegUtil.NO_NAME);
		}
		startService(serviceIntent);
	}
	
	private void checkBtAvailability(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}
		if (!bluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        Log.d(EegUtil.TAG, "Device Name"+device.getName() + "\n" + device.getAddress());
		    }
		}
	}

}