package com.phantomLord.cpufrequtils.app.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
	long hours, minute, second;

	public void calculateTime(long seconds) {
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
		minute = TimeUnit.SECONDS.toMinutes(seconds)
				- (TimeUnit.SECONDS.toHours(seconds) * 60);
		second = TimeUnit.SECONDS.toSeconds(seconds)
				- (TimeUnit.SECONDS.toMinutes(seconds) * 60);
	}

	public long getHours() {
		return hours;
	}

	public long getminutes() {
		return minute;
	}

	public long getSeconds() {
		return second;
	}

}
