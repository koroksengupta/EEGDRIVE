package com.android.eegdrive.popup;

import java.util.HashMap;
import java.util.Map;

import com.android.eegdrive.R;
import com.android.eegdrive.R.id;
import com.android.eegdrive.R.layout;
import com.android.eegdrive.popup.CircularSeekBar.OnSeekChangeListener;
import com.android.eegdrive.sendinfo.ReadWriteHumanResponseToCsvFile;
import com.android.eegdrive.util.EegUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Distance extends Activity{
	
	private static final int MAX_DISTANCE_TO_HOME = 100;
	private static final int MIN_DISTANCE_TO_HOME = 0;
	
	private CircularSeekBar mCirSeekbar;
	
	private Button mDone;
	
	private Map<String, String> mInfo = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.distance_to_home);
//		mStartTime = System.currentTimeMillis();
		long startTime = EegUtil.getTime();
		mInfo.put(EegUtil.ACTIVITY_NAME, "Distance");
		mInfo.put(EegUtil.START_TIME, Long.toString(startTime));
		mCirSeekbar = (CircularSeekBar) findViewById(R.id.distance_to_home);
		mCirSeekbar.setMaxProgress(MAX_DISTANCE_TO_HOME);
		mCirSeekbar.setProgress(MIN_DISTANCE_TO_HOME);
		mCirSeekbar.setBarWidth(50);
		mCirSeekbar.invalidate();
		mCirSeekbar.setSeekBarChangeListener(new OnSeekChangeListener(){

			@Override
			public void onProgressChange(CircularSeekBar view, int newProgress) {
				Log.d("Welcome", "Progress:" + view.getProgress() + "/" + view.getMaxProgress());
				mCirSeekbar.setText(Integer.toString(view.getProgress()));
			}
			
		});
		mDone = (Button) findViewById(R.id.done);
		mDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				long endTime = System.currentTimeMillis();
				long endTime = EegUtil.getTime();
				mInfo.put(EegUtil.END_TIME, Long.toString(endTime));
				ReadWriteHumanResponseToCsvFile humanResp = new ReadWriteHumanResponseToCsvFile();
				humanResp.generateHumanResponseCsv(mInfo);
				finish();
			}
		});
	}
}
