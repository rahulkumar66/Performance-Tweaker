package com.phantomLord.cpufrequtils.app.utils;

import android.content.Context;
import android.os.Build;

import com.asksven.android.common.kernelutils.AlarmsDumpsys;
import com.asksven.android.common.kernelutils.NativeKernelWakelock;
import com.asksven.android.common.kernelutils.Wakelocks;
import com.asksven.android.common.kernelutils.WakeupSources;
import com.asksven.android.common.privateapiproxies.Alarm;
import com.asksven.android.common.privateapiproxies.BatteryInfoUnavailableException;
import com.asksven.android.common.privateapiproxies.BatteryStatsProxy;
import com.asksven.android.common.privateapiproxies.BatteryStatsTypes;
import com.asksven.android.common.privateapiproxies.StatElement;
import com.asksven.android.common.privateapiproxies.Wakelock;
import com.phantomLord.cpufrequtils.app.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BatteryStatsUtils {
    /*
     * This class acts as a bridge between Android-Common Library and the rest
	 * of the Application
	 */

    public static ArrayList<NativeKernelWakelock> getNativeKernelWakelocks(
            Context mContext, boolean filterZeroValues) {

        ArrayList<NativeKernelWakelock> nativeKernelWakelocks = new ArrayList<NativeKernelWakelock>();
        ArrayList<StatElement> kernelWakelocks;
        if (Wakelocks.fileExists()) {
            kernelWakelocks = Wakelocks.parseProcWakelocks(mContext);
        } else {
            kernelWakelocks = WakeupSources.parseWakeupSources(mContext);
        }

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
        ArrayList<Wakelock> myWakelocks = new ArrayList<Wakelock>();
        ArrayList<StatElement> cpuWakelocks = new ArrayList<StatElement>();
		/*
		 * code for kitkat is missing
		 */
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                throw new BatteryInfoUnavailableException(
                        "Battery info not available");
            } catch (BatteryInfoUnavailableException e) {
                e.printStackTrace();
            }

            myWakelocks.add(new Wakelock(1,
                    "Feature not currently supported for kitkat", 0, 0, 0));

            return myWakelocks;
        }
        BatteryStatsProxy stats = BatteryStatsProxy.getInstance(context);
        try {
            cpuWakelocks = stats.getWakelockStats(context,
                    BatteryStatsTypes.WAKE_TYPE_PARTIAL,
                    BatteryStatsTypes.STATS_CURRENT, 0);
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

    public static ArrayList<Alarm> getAlarmStats(Context context) {
        ArrayList<Alarm> myWakelocks = new ArrayList<Alarm>();
        ArrayList<StatElement> alarms;
        if (SysUtils.isRooted()) {
            alarms = AlarmsDumpsys.getAlarms();
        } else {
            myWakelocks.add(new Alarm(context.getString(R.string.noroot)));
            return myWakelocks;
        }

        for (StatElement statElement : alarms) {
            Alarm alarm = (Alarm) statElement;

            if (alarm.getWakeups() > 0)
                myWakelocks.add(alarm);

        }
        Collections.sort(myWakelocks);

        return myWakelocks;

    }

    static void serializeReferences(WakelockReference wr, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput("aaa",
                    Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(fos);
            outputStream.writeObject(wr);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
