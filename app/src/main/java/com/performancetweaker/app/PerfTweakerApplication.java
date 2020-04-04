package com.performancetweaker.app;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.facebook.ads.AudienceNetworkAds;

import io.fabric.sdk.android.Fabric;

public class PerfTweakerApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        PerfTweakerApplication.context = getApplicationContext();
        AudienceNetworkAds.initialize(this);
    }

    public static Context getAppContext() {
        return PerfTweakerApplication.context;
    }
}