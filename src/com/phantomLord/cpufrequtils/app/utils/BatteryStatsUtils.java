package com.phantomLord.cpufrequtils.app.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.os.Build;

import com.asksven.android.common.kernelutils.NativeKernelWakelock;
import com.asksven.android.common.kernelutils.Wakelocks;
import com.asksven.android.common.privateapiproxies.BatteryStatsProxy;
import com.asksven.android.common.privateapiproxies.BatteryStatsTypes;
import com.asksven.android.common.privateapiproxies.StatElement;
import com.asksven.android.common.privateapiproxies.Wakelock;

public class BatteryStatsUtils {
	/*
	 * This class acts as a bridge between Android-Common Library and the rest
	 * of the Application
	 */

	public static ArrayList<NativeKernelWakelock> getNativeKernelWakelocks(
			Context mContext, boolean filterZeroValues) {

		ArrayList<NativeKernelWakelock> nativeKernelWakelocks = new ArrayList<>();
		ArrayList<StatElement> kernelWakelocks = Wakelocks
				.parseProcWakelocks(mContext);

		for (StatElement statElement : kernelWakelocks) {
			NativeKernelWakelock wakelock = (NativeKernelWakelock) statElement;
			if (filterZeroValues) {
				if (wakelock.getDuration() / 1000 > 0) {
					nativeKernelWakelocks.add(wakelock);
				}
			} else {
				nativeKernelWakelocks.add(wakelock);
			}
		}
		/*
		 * sort the data on the basis of duration
		 */
		Comparator<NativeKernelWakelock> timeComaprator = new NativeKernelWakelock.TimeComparator();
		Collections.sort(nativeKernelWakelocks, timeComaprator);
		return nativeKernelWakelocks;
	}

	public static ArrayList<Wakelock> getCpuWakelocksStats(Context context,
			boolean filterZeroValues) {
		/*
		 * code for kitkat is missing
		 */
		if (Build.VERSION.SDK_INT >= 19) {

		}
		BatteryStatsProxy stats = BatteryStatsProxy.getInstance(context);
		ArrayList<Wakelock> myWakelocks = new ArrayList<Wakelock>();
		ArrayList<StatElement> cpuWakelocks = new ArrayList<>();
		try {
			cpuWakelocks = stats.getWakelockStats(context,
					BatteryStatsTypes.WAKE_TYPE_PARTIAL,
					BatteryStatsTypes.STATS_SINCE_UNPLUGGED, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < cpuWakelocks.size(); i++) {
			Wakelock wl = (Wakelock) cpuWakelocks.get(i);
			if (filterZeroValues) {
				if ((wl.getDuration() / 1000) > 0) {
					myWakelocks.add(wl);
				}
			} else {
				myWakelocks.add(wl);
			}

		}

		/*
		 * Sort the data
		 */
		Comparator<Wakelock> comparator = new Wakelock.WakelockTimeComparator();
		Collections.sort(myWakelocks, comparator);

		return myWakelocks;
	}
}
