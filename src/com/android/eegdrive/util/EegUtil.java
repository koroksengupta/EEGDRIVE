package com.android.eegdrive.util;

import java.io.File;

import com.android.eegdrive.SntpClient;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

public class EegUtil {
	public static final String TAG = "EEG";

	public static String ATTENTION = "Attention";

	public static String MEDITATION = "Meditation";

	public static String SERVER_URL = "http://192.168.0.105:8081";

	public static final String DRIVER_NAME = "driver_name";

	public static final String NO_NAME = "Unknown Driver";

	public static final String TITLE = "BT Connection State";

	public static final String CONNECTING_TAG = "Connecting...";

	public static final String CONNECTED_TAG = "Connected.";

	public static final String NOT_FOUND_TAG = "Cannot locate BT EEG device";

	public static final String NOT_PAIRED_TAG = "BT Devices not paired.";

	public static final String DISCONNECT_TAG = "Disconnected.";

	public static final String DRIVE_START_STATUS = "Drive Started. Data is being recorded.";

	public static final String PROXIMITY_ALERT_INTENT = "com.android.eegdrive.ProximityAlert";
	
	public static final String POOR_SIGNAL = "PoorSignal";
	
	public static final String GET_RAW = "Got raw";
	
	public static final String HEART_RATE = "Heart rate";
	
	public static final String BLINK = "Blink";
	
	public static final String RAW_COUNT = "Raw Count";
	
	public static final String RAW_MULTI = "Raw1";
	
	public static final String LOW_BATTERY = "Low Battery!";

	public static final double LATITUDE_1 = 49.632589;

	public static final double LONGITUDE_1 = 6.168786;

	public static final double LATITUDE_2 = 49.61958;

	public static final double LONGITUDE_2 = 6.126440;

	public static final long POINT_RADIUS = 100; // in Meters

	public static final long PROX_ALERT_EXPIRATION = -1; // It will never
															// expire

	public static final String TIME = "Time";

	public static final String ACTIVITY_NAME = "Popup Name";

	public static final String RESPONSE_TIME = "Response Time (in ms)";

	public static final String NTP_SERVER_ADDRESS = "0.lu.pool.ntp.org";

	public static final String START_TIME = "start_time";

	public static final String END_TIME = "end_time";

	public static long getTime() {
		long timeNow = 0;
		SntpClient client = new SntpClient();
		if (client.requestTime(NTP_SERVER_ADDRESS, 100)) {
			timeNow = client.getNtpTime() + SystemClock.elapsedRealtime()
					- client.getNtpTimeReference();
			Log.d(EegUtil.TAG, "Time: " + timeNow);
		}
		return timeNow;
	}
	
	public static File getBCIFolder() {
		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/BCIInfo");
		Log.d(TAG, "Folder Path: " + folder.toString());
		if (!folder.exists()) {
			folder.mkdir();
		}
		return folder;
	}// End of getting the BCI FOlder

}
