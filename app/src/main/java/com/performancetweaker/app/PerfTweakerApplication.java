package com.performancetweaker.app;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.analytics.FirebaseAnalytics;

public class PerfTweakerApplication extends MultiDexApplication {
    private static FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public static FirebaseAnalytics getFirebaseAnalyticsInstance() {
        return firebaseAnalytics;
    }
}