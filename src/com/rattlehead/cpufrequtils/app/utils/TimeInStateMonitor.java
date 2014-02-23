package com.rattlehead.cpufrequtils.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

public class TimeInStateMonitor implements Constants {
	SparseArray<Long> offsets = new SparseArray<Long>();
	SparseIntArray x=new SparseIntArray();

	ArrayList<CpuState> states = new ArrayList<CpuState>();
	CpuState _offsets;
	private long totaltime = 0;

	public TimeInStateMonitor() {

	}

	public ArrayList<CpuState> getCpuStateTime() {
		states.clear();
		String line;
		BufferedReader bufferedReader = RootUtils.getBufferForCommand("cat "
				+ Constants.time_in_states);
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String entries[] = line.split(" ");
				states.add(new CpuState(Integer.parseInt(entries[0]), Long
						.parseLong(entries[1])));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		 long deepSleepTime= (SystemClock.elapsedRealtime()-
		 SystemClock.uptimeMillis()/100);
		// states.add(new CpuState(0,deepSleepTime));
		 Log.d(tag,"deep "+deepSleepTime);
		return states;
	}

	public long getTotalTimeInState() {
		totaltime = 0;
		for (CpuState state : states) {
			totaltime += state.getTime();
		}

		Log.d(tag, "inside method " + totaltime);
		return totaltime;
	}

	public void clearCpuStates() {
		states.clear();
	}

}
