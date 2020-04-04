package com.performancetweaker.app.utils;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class FANInterstialHelper {

    private static InterstitialAd interstitialAd;
    private static String TAG = Constants.App_Tag;
    private static FANInterstialHelper interstialHelper;

    private FANInterstialHelper(Context context) {
        interstitialAd = new InterstitialAd(context, Constants.INTERSTITIAL_AD_PLACEMENT_ID);
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.e(TAG, "FAN Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.e(TAG, "FAN Interstitial ad dismissed.");
                interstitialAd.loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "FAN Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "FAN: Interstitial ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "FAN Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "FAN Interstitial ad impression logged!");
            }
        });
    }

    public static FANInterstialHelper getInstance(Context ctx) {
        if (interstialHelper == null) {
            interstialHelper = new FANInterstialHelper(ctx);
        }
        return interstialHelper;
    }

    public void loadAd() {
        interstitialAd.loadAd();
    }

    public void showAd() {
        // Check if interstitialAd has been loaded successfully
        if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
            return;
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if(interstitialAd.isAdInvalidated()) {
            interstitialAd.loadAd();
            return;
        }
        // Show the ad
        interstitialAd.show();
    }

    public void destroyAd() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
    }
}
