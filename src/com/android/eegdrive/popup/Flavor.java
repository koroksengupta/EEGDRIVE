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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

public class Flavor extends Activity {
	
	private Spinner mIceCream;
	
	private Button mDone;
	
	private Map<String, String> mInfo = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ice_cream_falvor);
//		mStartTime = System.currentTimeMillis();
		long startTime = EegUtil.getTime();
		mInfo.put(EegUtil.ACTIVITY_NAME, "Flavor");
		mInfo.put(EegUtil.START_TIME, Long.toString(startTime));
		mIceCream = (Spinner) findViewById(R.id.spinner1);
		mIceCream.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
//				String iceCreamFlavor = parent.getItemAtPosition(pos).toString();				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
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
