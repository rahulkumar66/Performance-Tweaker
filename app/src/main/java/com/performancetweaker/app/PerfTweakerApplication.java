package com.performancetweaker.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class PerfTweakerApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        PerfTweakerApplication.context = getApplicationContext();
//        AudienceNetworkAds.initialize(this);
    }

    public static Context getAppContext() {
        return PerfTweakerApplication.context;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}