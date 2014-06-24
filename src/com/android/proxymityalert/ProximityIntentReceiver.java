package com.android.proxymityalert;

import java.util.Random;

import com.android.proxymityalert.R;
import com.android.proxymityalert.popup.Calculator;
import com.android.proxymityalert.popup.Distance;
import com.android.proxymityalert.popup.FavoriteColor;
import com.android.proxymityalert.popup.Flavor;
import com.android.proxymityalert.popup.YourAge;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver {
	
	private static final String TAG = "ProximityAlert";
	
	private Context mContext;
	
	private static final int NOTIFICATION_ID = 1000;

	private static final int CALCULATOR_INSTANCE = 1;

	private static final int DISTANCE_INSTANCE = 2;

	private static final int COLOR_INSTANCE = 3;

	private static final int FLAVOR_INSTANCE = 4;

	private static final int AGE_INSTANCE = 5;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);
		if (entering) {
			Log.d(TAG, "entering");
			startNewActivity(mContext);
		} else {
			Log.d(TAG, "exiting");
		}
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(context,
				ProximityAlertActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		Notification notification = createNotification();
		notification.setLatestEventInfo(context, "Proximity Alert!",
				"You are near your point of interest.", pendingIntent);

		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	private Notification createNotification() {
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.when = System.currentTimeMillis();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.ledARGB = Color.WHITE;
		notification.ledOnMS = 1500;
		notification.ledOffMS = 1500;
		return notification;
	}

	private void startNewActivity(Context context) {
		int randomActivity = getRandomNumber();
		Intent intent = null;
		Log.d(TAG, "Coming in StartNewActivity");
		if (CALCULATOR_INSTANCE == randomActivity) {
			intent = new Intent(context, Calculator.class);
			mContext.startActivity(intent);
		} else if (DISTANCE_INSTANCE == randomActivity) {
			intent = new Intent(context, Distance.class);
			mContext.startActivity(intent);
		} else if (COLOR_INSTANCE == randomActivity) {
			intent = new Intent(context, FavoriteColor.class);
			mContext.startActivity(intent);
		} else if (FLAVOR_INSTANCE == randomActivity) {
			intent = new Intent(context, Flavor.class);
			mContext.startActivity(intent);
		} else if (AGE_INSTANCE == randomActivity) {
			intent = new Intent(context, YourAge.class);
			mContext.startActivity(intent);
		}

	}

	private int getRandomNumber() {
		Random rn = new Random();
		return rn.nextInt(5) + 1;
	}
}
