package com.android.eegdrive;

import com.android.eegdrive.sendinfo.WriteToCSVFile;
import com.android.eegdrive.util.EegUtil;
import com.neurosky.thinkgear.TGDevice;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class GetEegData extends Service {

	BluetoothAdapter mBluetoothAdapter;
	TGDevice tgDevice;
	final boolean rawEnabled = false;
	public static String mAttention = null;
	public static String mMeditation = null;
	private ProgressDialog pDialog = null;
	private Context mContext;
	WriteToCSVFile mCsvWriter = new WriteToCSVFile();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mContext = this;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		startTgDevice();
		Log.d(EegUtil.TAG, "Coming to onStartCommand");

		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void startTgDevice() {
		if (mBluetoothAdapter == null) {
			// Alert user that Bluetooth is not available
			Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG)
					.show();
			return;
		} else {
			/* create the TGDevice */
			tgDevice = new TGDevice(mBluetoothAdapter, handler);
			if (tgDevice.getState() != TGDevice.STATE_CONNECTING
					&& tgDevice.getState() != TGDevice.STATE_CONNECTED)
				tgDevice.connect(rawEnabled);
		}
	}

	/**
	 * Handles messages from TGDevice
	 */
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.d(EegUtil.TAG, "Which Message: " + msg.what);
			Log.d(EegUtil.TAG, "Args of Message: " + msg.arg1);
			switch (msg.what) {
			case TGDevice.MSG_STATE_CHANGE:
				switch (msg.arg1) {
				case TGDevice.STATE_IDLE:
					break;
				case TGDevice.STATE_CONNECTING:
					showProgressDialog(EegUtil.CONNECTING_TAG);
					break;
				case TGDevice.STATE_CONNECTED:
					cancelProgressDialog();
					showAlertDialog(EegUtil.CONNECTED_TAG);
					tgDevice.start();
					break;
				case TGDevice.STATE_NOT_FOUND:
					stopSelf();
					cancelProgressDialog();
					showAlertDialog(EegUtil.NOT_FOUND_TAG);
					break;
				case TGDevice.STATE_NOT_PAIRED:
					stopSelf();
					cancelProgressDialog();
					showAlertDialog(EegUtil.NOT_PAIRED_TAG);
					break;
				case TGDevice.STATE_DISCONNECTED:
					stopSelf();
					cancelProgressDialog();
					showAlertDialog(EegUtil.DISCONNECT_TAG);
				}

				break;
			case TGDevice.MSG_POOR_SIGNAL:
				mCsvWriter.generateCsvFile(EegUtil.POOR_SIGNAL,
						String.valueOf(msg.arg1));
				// signal = msg.arg1;
				// tv.append("PoorSignal: " + msg.arg1 + "\n");
				break;
			case TGDevice.MSG_RAW_DATA:
				mCsvWriter.generateCsvFile(EegUtil.GET_RAW,
						String.valueOf(msg.arg1));
				// raw1 = msg.arg1;
				// tv.append("Got raw: " + msg.arg1 + "\n");
				break;
			case TGDevice.MSG_HEART_RATE:
				mCsvWriter.generateCsvFile(EegUtil.HEART_RATE,
						String.valueOf(msg.arg1));
				// tv.append("Heart rate: " + msg.arg1 + "\n");
				break;
			case TGDevice.MSG_ATTENTION:
				int att = msg.arg1;
				mAttention = String.valueOf(att);
				mCsvWriter.generateCsvFile(EegUtil.ATTENTION, mAttention);

				// tv.append("Attention: " + msg.arg1 + "\n");
				// Log.v("HelloA", "Attention: " + att + "\n");
				break;
			case TGDevice.MSG_MEDITATION:
				int med = msg.arg1;
				mMeditation = String.valueOf(med);
				mCsvWriter.generateCsvFile(EegUtil.MEDITATION, mMeditation);

				// tv.append("Meditation: " + msg.arg1 + "\n");
				break;
			case TGDevice.MSG_BLINK:
				mCsvWriter.generateCsvFile(EegUtil.BLINK,
						String.valueOf(msg.arg1));
				// tv.append("Blink: " + msg.arg1 + "\n");
				break;
			case TGDevice.MSG_RAW_COUNT:
				mCsvWriter.generateCsvFile(EegUtil.RAW_COUNT,
						String.valueOf(msg.arg1));
				// tv.append("Raw Count: " + msg.arg1 + "\n");
				break;
			case TGDevice.MSG_LOW_BATTERY:
				showAlertDialog(EegUtil.LOW_BATTERY);
				Toast.makeText(getApplicationContext(), "Low battery!",
						Toast.LENGTH_SHORT).show();
				break;
			case TGDevice.MSG_RAW_MULTI:
				mCsvWriter.generateCsvFile(EegUtil.RAW_MULTI,
						String.valueOf(msg.arg1));
				// TGRawMulti rawM = (TGRawMulti)msg.obj;
				// tv.append("Raw1: " + rawM.ch1 + "\nRaw2: " + rawM.ch2);
			default:
				break;
			}
		}
	};

	private void showProgressDialog(String msg) {
		pDialog = new ProgressDialog(mContext);
		pDialog.setTitle("Please wait...");
		pDialog.setMessage(msg);
		pDialog.setCancelable(true);
		pDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		pDialog.show();

	}

	private void cancelProgressDialog() {
		if (pDialog != null) {
			pDialog.cancel();
		}
	}

	private void showAlertDialog(String msg) {
		final String message = msg;
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setTitle(EegUtil.TITLE).setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int flag) {
						if (message.equals(EegUtil.CONNECTED_TAG)) {
							Intent intent = new Intent(GetEegData.this,
									StartCarDrive.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						} else{
							dialog.dismiss();
						}

					}
				});

		AlertDialog dialog = alertBuilder.create();
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}

	@Override
	public void onDestroy() {
		Log.d(EegUtil.TAG, "Stopping the Service");
		tgDevice.stop();
	}

}
