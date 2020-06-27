package com.performancetweaker.app;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class PerfTweakerApplication extends MultiDexApplication {
    private static FirebaseAnalytics firebaseAnalytics;
    private static FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public static FirebaseAnalytics getFirebaseAnalyticsInstance() {
        return firebaseAnalytics;
    }

    public static FirebaseRemoteConfig getFirebaseRemoteConfig() {
        if (mFirebaseRemoteConfig == null) {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        }
        return mFirebaseRemoteConfig;
    }
}