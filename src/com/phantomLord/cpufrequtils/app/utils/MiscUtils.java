package com.phantomLord.cpufrequtils.app.utils;

import java.util.ArrayList;

import com.asksven.android.common.privateapiproxies.StatElement;

public class MiscUtils {

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

}
