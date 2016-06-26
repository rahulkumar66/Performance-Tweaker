package com.rattlehead666.performancetweaker.app.services;

import com.rattlehead666.performancetweaker.app.utils.Constants;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class SaveSinceUnpluggedReferenceService extends IntentService {

    public SaveSinceUnpluggedReferenceService() {
        super("Save Unplugged reference service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Constants.App_Tag, "Saving Unplugged References ");

        stopSelf();
    }
}
