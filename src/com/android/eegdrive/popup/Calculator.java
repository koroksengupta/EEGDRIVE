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

public class Calculator extends Activity{
	
	private Button mFirst;
	
	private Button mSecond;
	
	private Button mThird;
	
	private Button mFourth;
	
	private Map<String, String> mInfo = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate);
//		mStartTime = System.currentTimeMillis();
		long startTime = EegUtil.getTime();
		mInfo.put(EegUtil.ACTIVITY_NAME, "Calculator");
		mInfo.put(EegUtil.START_TIME, Long.toString(startTime));
		initButton();
	}
	
	private void initButton(){
		mFirst = (Button) findViewById(R.id.answer1);
		
		mSecond = (Button) findViewById(R.id.answer2);
		
		mThird = (Button) findViewById(R.id.answer3);
		
		mFourth = (Button) findViewById(R.id.answer4);
		mFirst.setOnClickListener(mButtonListener);
		mSecond.setOnClickListener(mButtonListener);
		mThird.setOnClickListener(mButtonListener);
		mFourth.setOnClickListener(mButtonListener);
	}
	
	private View.OnClickListener mButtonListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
//			long endTime = System.currentTimeMillis();
			long endTime = EegUtil.getTime();
			mInfo.put(EegUtil.END_TIME, Long.toString(endTime));
			ReadWriteHumanResponseToCsvFile humanResp = new ReadWriteHumanResponseToCsvFile();
			humanResp.generateHumanResponseCsv(mInfo);
			finish();
		}
	};
}
