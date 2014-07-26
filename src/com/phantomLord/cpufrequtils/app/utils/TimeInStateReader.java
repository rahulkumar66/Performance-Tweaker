package com.phantomLord.cpufrequtils.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import android.os.SystemClock;

public class TimeInStateReader {
	private ArrayList<CpuState> states = new ArrayList<>();
	private ArrayList<CpuState> newStates = new ArrayList<>();

	private long totaltime;

	public TimeInStateReader() {
	}

	public TimeInStateReader getInstance() {
		return new TimeInStateReader();
	}

	public ArrayList<CpuState> getCpuStateTime(boolean withDeepSleep) {
		states.clear();
		BufferedReader bufferedReader;
		Process process = null;
		File statsFile = new File(Constants.time_in_states);
		if (statsFile.exists()) {
			if (statsFile.canRead()) {
				String line;
				try {
					process = Runtime.getRuntime().exec(
							"cat " + Constants.time_in_states);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				bufferedReader = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				try {
					while ((line = bufferedReader.readLine()) != null) {
						String entries[] = line.split(" ");
						long time = Long.parseLong(entries[1]);
						states.add(new CpuState(Integer.parseInt(entries[0]),
								time));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		/*
		 * calculate deep sleep time
		 */

		if (withDeepSleep) {
			long deepSleepTime = (SystemClock.elapsedRealtime() - SystemClock
					.uptimeMillis()) / 10;
			if (deepSleepTime > 0)
				states.add(new CpuState(0, deepSleepTime));
		}
		if (states != null) {
			Collections.sort(states);
		}

		if (newStates.size() > 0) {
			for (int i = 0; i < newStates.size(); i++) {
				states.get(i).time -= newStates.get(i).time;
			}
		}
		return states;
	}

	public long getTotalTimeInState() {
		totaltime = 0;
		for (CpuState state : states) {
			totaltime += state.getTime();
		}
		return totaltime;
	}

	public void setNewStates(ArrayList<CpuState> state) {
		clearNewStates();
		newStates = state;
	}

	public void clearNewStates() {
		newStates.clear();
	}

}
