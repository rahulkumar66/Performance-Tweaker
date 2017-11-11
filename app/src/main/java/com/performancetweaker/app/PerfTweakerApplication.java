package com.performancetweaker.app;

import android.app.Application;
import android.content.Context;

public class PerfTweakerApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        PerfTweakerApplication.context = getApplicationContext();
    }


    public static Context getAppContext()
    {
        return PerfTweakerApplication.context;
    }
}