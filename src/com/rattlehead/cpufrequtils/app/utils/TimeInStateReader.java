package com.rattlehead.cpufrequtils.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.os.SystemClock;

public class TimeInStateReader implements Constants {
	private ArrayList<CpuState> states;

	private static long totaltime;

	public TimeInStateReader() {
		states=new ArrayList<CpuState>();
		totaltime = 0;
		
	}

	public ArrayList<CpuState> getCpuStateTime() {
		states.clear();
		String line;
		BufferedReader bufferedReader = RootUtils.getBufferForCommand("cat "
				+ Constants.time_in_states);
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String entries[] = line.split(" ");
				long time = Long.parseLong(entries[1]) / 100;
				states.add(new CpuState(Integer.parseInt(entries[0]), time));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long deepSleepTime = (SystemClock.elapsedRealtime() - SystemClock
				.uptimeMillis());
		long seconds = TimeUnit.MILLISECONDS.toSeconds(deepSleepTime);
		if (deepSleepTime > 0)
			states.add(new CpuState(0, seconds));
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
