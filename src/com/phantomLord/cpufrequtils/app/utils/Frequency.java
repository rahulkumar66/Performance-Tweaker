package com.phantomLord.cpufrequtils.app.utils;

public class Frequency {
	int freq;
	String frequency;

	private Frequency(String frq) {
		this.frequency = frq;
	}

	@Override
	public String toString() {
		return String.valueOf(frequency + " Mhz");
	}

}