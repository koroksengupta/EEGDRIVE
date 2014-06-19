package com.android.eegdrive.sendinfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.android.eegdrive.util.EegUtil;

import android.text.format.Time;
import android.util.Log;

public class ReadWriteHumanResponseToCsvFile {

	private static final String TAG = "ReadWriteHumanResponseToCsvFile";

	private static final String CSV_FILE_RESPONSE_INFO = "TwizzyHumanResponse.csv";

	private static final String CSV_SEPARATOR = ",";

	private static final String END_OF_LINE = "\n";

	private String mPath = null;

	public ReadWriteHumanResponseToCsvFile() {
		EegUtil.getBCIFolder();
	}

	public void generateHumanResponseCsv(Map<String, String> info) {
		this.mPath = EegUtil.getBCIFolder().toString() + "/"
				+ CSV_FILE_RESPONSE_INFO;
		final Map<String, String> map = info;
		new Thread() {
			public void run() {
				String name = null;
				String startTime = null;
				String endTime = null;
				Iterator itr = map.entrySet().iterator();
				while (itr.hasNext()) {
					Map.Entry entry = (Map.Entry) itr.next();
					if (EegUtil.ACTIVITY_NAME.equals(entry.getKey().toString())) {
						name = entry.getValue().toString();
					} else if (EegUtil.START_TIME.equals(entry.getKey()
							.toString())) {
						startTime = entry.getValue().toString();
					} else if (EegUtil.END_TIME.equals(entry.getKey()
							.toString())) {
						endTime = entry.getValue().toString();
					}
				}
				Time time = new Time();
				time.setToNow();
				try {
					FileWriter csvWriter = new FileWriter(mPath, true);
					if (!readCsv()) {
						csvWriter.append(EegUtil.TIME);
						csvWriter.append(CSV_SEPARATOR);
						csvWriter.append(EegUtil.ACTIVITY_NAME);
						csvWriter.append(CSV_SEPARATOR);
						csvWriter.append(EegUtil.START_TIME);
						csvWriter.append(CSV_SEPARATOR);
						csvWriter.append(EegUtil.END_TIME);
						csvWriter.append(END_OF_LINE);
					}
					csvWriter.append(Integer.toString(time.hour) + ":"
							+ Integer.toString(time.minute) + ":" + Integer.toString(time.second));
					csvWriter.append(CSV_SEPARATOR);
					csvWriter.append(name);
					csvWriter.append(CSV_SEPARATOR);
					csvWriter.append(startTime);
					csvWriter.append(CSV_SEPARATOR);
					csvWriter.append(endTime);
					csvWriter.append(END_OF_LINE);
					csvWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	public boolean readCsv() {
		boolean isHeaderPresent = false;
		BufferedReader br = null;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(mPath));
			while ((line = br.readLine()) != null) {
				String[] info = line.split(CSV_SEPARATOR);
				Log.d(TAG, "Time in first line: " + info[0]);
				if (EegUtil.TIME.equals(info[0])) {
					isHeaderPresent = true;
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isHeaderPresent;
	}

}
