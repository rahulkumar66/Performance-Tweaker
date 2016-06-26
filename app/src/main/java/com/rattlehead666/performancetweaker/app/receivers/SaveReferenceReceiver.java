package com.rattlehead666.performancetweaker.app.receivers;

import com.rattlehead666.performancetweaker.app.services.SaveSinceUnpluggedReferenceService;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class SaveReferenceReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
            context.startService(new Intent(context, SaveSinceUnpluggedReferenceService.class));
        }
    }
}
