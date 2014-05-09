package com.phantomLord.cpufrequtils.app.utils;

public class CpuState {

	private int frequency;
	private long time;

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

}
