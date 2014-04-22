package com.rattlehead.cpufrequtils.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.os.SystemClock;

public class TimeInStateReader implements Constants {
	private ArrayList<CpuState> states;

	private static long totaltime;

	public TimeInStateReader() {
		states = new ArrayList<CpuState>();
		totaltime = 0;
	}

	public ArrayList<CpuState> getCpuStateTime(boolean withDeepSleep) {
		states.clear();
		BufferedReader bufferedReader;
		File statsFile = new File(time_in_states);
		if (statsFile.exists()) {
			if (statsFile.canRead()) {
				String line;
				Process process = null;
				try {
					process = Runtime.getRuntime()
							.exec("cat " + time_in_states);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				bufferedReader = new BufferedReader(new InputStreamReader(
						process.getInputStream()));

				try {
					while ((line = bufferedReader.readLine()) != null) {
						String entries[] = line.split(" ");
						long time = Long.parseLong(entries[1]) / 100;
						if(time>0) {
						states.add(new CpuState(Integer.parseInt(entries[0]),
								time));
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				/*
				 * calculate deep sleep time
				 */
				if (withDeepSleep) {
					long deepSleepTime = (SystemClock.elapsedRealtime() - SystemClock
							.uptimeMillis());
					long seconds = TimeUnit.MILLISECONDS
							.toSeconds(deepSleepTime);
					if (deepSleepTime > 0)
						states.add(new CpuState(0, seconds));
				}
			}
		}
		return states;
	}

	public long getTotalTimeInState() {

		for (CpuState state : states) {
			totaltime += state.getTime();
		}
		return totaltime;
	}

	public void clearCpuStates() {
		states.clear();
	}
}
