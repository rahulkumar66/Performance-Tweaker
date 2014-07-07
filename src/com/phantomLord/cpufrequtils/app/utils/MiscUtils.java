package com.phantomLord.cpufrequtils.app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.asksven.android.common.privateapiproxies.StatElement;
import com.phantomLord.cpufrequtils.app.R;

public class MiscUtils {
	public static final Map<String, Integer> THEMES_MAP = new HashMap<String, Integer>() {
		{
			put("Dark", R.style.Theme_Sherlock);
			put("Light", R.style.Theme_Sherlock_Light);
			put("Light_DarkActionBar",
					R.style.Theme_Sherlock_Light_DarkActionBar);
		}
	};

	public static ArrayList<StatElement> removeZeroValues(
			ArrayList<StatElement> items) {
		Object object[] = new Object[items.size()];
		int i = 0;
		for (StatElement item : items) {
			double val[] = item.getValues();
			if (val[0] == 0) {
				object[i] = item;
				i++;
			}
		}
		for (Object o : object) {
			items.remove(o);
		}
		return items;

	}

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
