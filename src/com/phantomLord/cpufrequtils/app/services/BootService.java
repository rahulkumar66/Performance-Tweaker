package com.phantomLord.cpufrequtils.app.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.phantomLord.cpufrequtils.app.utils.Constants;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;

public class BootService extends Service {
	SharedPreferences prefs;
	Context context;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.PREF_TIS_RESET_STATS, null);
		editor.commit();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (SysUtils.isRooted()) {
			if (prefs.getBoolean(Constants.PREF_CPU_APPLY_ON_BOOT, false)) {
				String max, min, gov;
				max = prefs.getString(Constants.PREF_MAX_FREQ, null);
				min = prefs.getString(Constants.PREF_MIN_FREQ, null);
				gov = prefs.getString(Constants.PREF_GOV, null);

				if (max != null || min != null || gov != null) {
					SysUtils.setFrequencyAndGovernor(max, min, gov, context);
				}
			}
			if (prefs.getBoolean(Constants.PREF_IO_APPLY_ON_BOOT, false)) {
				String ioscheduler = prefs.getString(
						Constants.PREF_IO_SCHEDULER, null);
				String readAhead = prefs.getString(Constants.PREF_READ_AHEAD,
						null);

				if (ioscheduler != null || readAhead != null) {
					SysUtils.setDiskSchedulerandReadAhead(ioscheduler,
							readAhead, context);
				}
			}
		}
		stopSelf();

	}
}
