package com.phantomLord.cpufrequtils.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BootService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

}
