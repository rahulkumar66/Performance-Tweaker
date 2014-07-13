package com.phantomLord.cpufrequtils.app.utils;

import java.util.HashMap;
import java.util.Map;

import com.phantomLord.cpufrequtils.app.R;

public class MiscUtils {
	public static final Map<String, Integer> THEMES_MAP = new HashMap<String, Integer>() {

		private static final long serialVersionUID = 1552737519285513057L;
		{
			put("Dark", R.style.Theme_Sherlock);
			put("Light", R.style.Theme_Sherlock_Light);
			put("Light_DarkActionBar",
					R.style.Theme_Sherlock_Light_DarkActionBar);
		}
	};

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
