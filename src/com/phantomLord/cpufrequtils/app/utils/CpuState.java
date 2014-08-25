package com.phantomLord.cpufrequtils.app.utils;

public class CpuState extends Object implements Comparable<CpuState> {

	public int frequency;
	public long time;

	public CpuState(int freq, long t) {
		this.frequency = freq;
		this.time = t;
	}

	public long getTime() {
		return time;
	}

	public int getFrequency() {
		return frequency;
	}

	@Override
	public int compareTo(CpuState state) {
		if (this.frequency < state.frequency)
			return 1;
		else if (this.frequency > state.frequency)
			return -1;
		else
			return 0;
	}

}
