package com.performancetweaker.app.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.performancetweaker.app.PerfTweakerApplication;

public class InterstialHelper {

    private static InterstialHelper interstialHelper;
    private static InterstitialAd adMobInterstitial;
    private static boolean showAdsFlag = false;
    private static Integer adShowCounter = 0;

    private static String TAG = Constants.App_Tag;

    private InterstialHelper(Context context, boolean showAds) {
        showAdsFlag = showAds;
        if (showAdsFlag) {
            adMobInterstitial = new InterstitialAd(context);
            adMobInterstitial.setAdUnitId(Constants.ABMOB_INTERSTITIAL_ID);
            adMobInterstitial.loadAd(new AdRequest.Builder().build());
            adMobInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    adMobInterstitial.loadAd(new AdRequest.Builder().build());
                }
            });
        }
    }

    public static InterstialHelper getInstance(Context ctx, boolean... optionalFlag) {
        if (interstialHelper == null) {
            boolean showAds = (optionalFlag.length >= 1) && optionalFlag[0];
            interstialHelper = new InterstialHelper(ctx, showAds);
        }
        return interstialHelper;
    }

    public void showAd() {
        if (showAdsFlag && adShowCounter % 2 == 0) {
            if (adMobInterstitial.isLoaded()) {
                adMobInterstitial.show();
                Bundle bundle = new Bundle();
                bundle.putString("AdShown", String.valueOf(true));
                PerfTweakerApplication.getFirebaseAnalyticsInstance().logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("shown", String.valueOf(true));
        PerfTweakerApplication.getFirebaseAnalyticsInstance().logEvent("showAdmobInterstitial", bundle);
        adShowCounter++;
    }

}
