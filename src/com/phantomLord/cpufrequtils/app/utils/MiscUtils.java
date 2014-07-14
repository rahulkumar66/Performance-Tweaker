package com.phantomLord.cpufrequtils.app.utils;


public class MiscUtils {

	public static String secToString(long tSec) {
		long h = (long) Math.floor(tSec / (60 * 60));
		long m = (long) Math.floor((tSec - h * 60 * 60) / 60);
		long s = tSec % 60;
		String sDur;
		sDur = h + ":";
		if (m < 10)
			sDur += "0";
		sDur += m + ":";
		if (s < 10)
			sDur += "0";
		sDur += s;

		return sDur;

	}

}
