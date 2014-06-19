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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class YourAge extends Activity {

	private EditText mAge;

	private Button mDone;
	
	private Map<String, String> mInfo = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.your_age);
//		mStartTime = System.currentTimeMillis();
		long startTime = EegUtil.getTime();
		mInfo.put(EegUtil.ACTIVITY_NAME, "Your Age");
		mInfo.put(EegUtil.START_TIME, Long.toString(startTime));
		mAge = (EditText) findViewById(R.id.add_age);
		mDone = (Button) findViewById(R.id.done);
		mDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String age = mAge.getText().toString();
				if (!age.isEmpty()) {
//					long endTime = System.currentTimeMillis();
					long endTime = EegUtil.getTime();
					mInfo.put(EegUtil.END_TIME, Long.toString(endTime));
					ReadWriteHumanResponseToCsvFile humanResp = new ReadWriteHumanResponseToCsvFile();
					humanResp.generateHumanResponseCsv(mInfo);
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Add your Age and then press Done",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
