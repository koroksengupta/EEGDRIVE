package com.android.eegdrive.popup;

import java.util.HashMap;
import java.util.Map;

import com.android.eegdrive.R;
import com.android.eegdrive.R.id;
import com.android.eegdrive.R.layout;
import com.android.eegdrive.sendinfo.ReadWriteHumanResponseToCsvFile;
import com.android.eegdrive.util.EegUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FavoriteColor extends Activity {

	private Button mDone;
	
	private Map<String, String> mInfo = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_color);
//		mStartTime = System.currentTimeMillis();
		long startTime = EegUtil.getTime();
		mInfo.put(EegUtil.ACTIVITY_NAME, "Favorite Color");
		mInfo.put(EegUtil.START_TIME, Long.toString(startTime));
		initButton();
	}

	private void initButton() {
		mDone = (Button) findViewById(R.id.done);
		mDone.setOnClickListener(new OnClickListener() {

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
