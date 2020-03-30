package com.performancetweaker.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.performancetweaker.app.services.BootService;
import com.performancetweaker.app.utils.Utils;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Utils.startService(context, new Intent(context, BootService.class));
        }
    }
}
