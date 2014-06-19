package com.android.eegdrive.sendinfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.android.eegdrive.util.EegUtil;

import android.text.format.Time;
import android.util.Log;

public class WriteToCSVFile {

	private static final String CSV_FILE = "BCIInfo.csv";

	private static final String CSV_SEPARATOR = ",";

	private static final String END_OF_LINE = "\n";

	private String mPath = null;

	public void WriteToCsvFile() {
		EegUtil.getBCIFolder();
	} // End Of Constructor

	public void generateCsvFile(String key, String info) {
		this.mPath = EegUtil.getBCIFolder().toString() + "/" + CSV_FILE;
		Log.d(EegUtil.TAG, "Coming in generateCsv");
		Log.d(EegUtil.TAG, "File Path immediately: "+mPath);
		final String keyInfo = key;
		final String val = info;
		new Thread() {
			public void run() {
				String att1 = "0.0";
				String med1 = "0.0";

				Time time = new Time();

				time.setToNow();

				try {
					// /////////////////////////////////////////
					if (mPath != null) {
						File file = new File(mPath);
						// if file doesnt exists, then create it
						if (!file.exists()) {
							file.createNewFile();
						}// End of Try block

						FileWriter csvWriter = new FileWriter(mPath, true);
						Log.d(EegUtil.TAG, "File Path: "+mPath);
						if (EegUtil.ATTENTION.equals(keyInfo)) {
							att1 = val;
						}
						if (EegUtil.MEDITATION.equals(keyInfo)) {
							med1 = val;
						}

						csvWriter.append(Integer.toString(time.hour) + ":"
								+ Integer.toString(time.minute) + ":" + Integer.toString(time.second));
						csvWriter.append(CSV_SEPARATOR);
						csvWriter.append(att1);
						csvWriter.append(CSV_SEPARATOR);
						csvWriter.append(med1);
						csvWriter.append(END_OF_LINE);
						csvWriter.close();
					} else {
						EegUtil.getBCIFolder();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
