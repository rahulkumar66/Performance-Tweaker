package com.performancetweaker.app.utils;

import android.content.Context;
import android.util.Log;

import com.appnext.ads.interstitial.Interstitial;
import com.appnext.core.callbacks.OnAdClosed;
import com.appnext.core.callbacks.OnAdError;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class InterstialHelper {

    private static InterstitialAd fanInterstitial;
    private static Interstitial appnextInterstitial;
    private static InterstialHelper interstialHelper;

    private static String TAG = Constants.App_Tag;

    private InterstialHelper(Context context) {
        fanInterstitial = new InterstitialAd(context, Constants.FAN_INTERSTITIAL_ID);
        fanInterstitial.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.i(TAG, "FAN Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.i(TAG, "FAN Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "FAN Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.i(TAG, "FAN: Interstitial ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.i(TAG, "FAN Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.i(TAG, "FAN Interstitial ad impression logged!");
            }
        });

        appnextInterstitial = new Interstitial(context, "99958b29-0802-49a5-bc73-27fa65582ada");
        appnextInterstitial.setOnAdErrorCallback(new OnAdError() {
            @Override
            public void adError(String error) {
                Log.d(TAG, "APPNEXT:ERROR" + error);
            }
        });
        appnextInterstitial.setOnAdClosedCallback(new OnAdClosed() {
            @Override
            public void onAdClosed() {
                Log.d(TAG, "APPNEXT:Ad Closed");
                interstialHelper.loadAd();
            }
        });
    }

    public static InterstialHelper getInstance(Context ctx) {
        if (interstialHelper == null) {
            interstialHelper = new InterstialHelper(ctx);
        }
        return interstialHelper;
    }

    public void loadAd() {
        if (!fanInterstitial.isAdLoaded() || fanInterstitial.isAdInvalidated()) {
            fanInterstitial.loadAd();
        }
        if (!appnextInterstitial.isAdLoaded()) {
            appnextInterstitial.loadAd();
        }
    }

    public void showAd() {
        if (appnextInterstitial.isAdLoaded()) {
            appnextInterstitial.showAd();
        } else if (fanInterstitial.isAdLoaded()) {
            fanInterstitial.show();
        }
    }

    public void destroyAd() {
        if (fanInterstitial != null) {
            fanInterstitial.destroy();
        }
        if (appnextInterstitial != null) {
            appnextInterstitial.destroy();
        }
    }
}
