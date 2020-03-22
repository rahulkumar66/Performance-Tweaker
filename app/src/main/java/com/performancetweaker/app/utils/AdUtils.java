package com.performancetweaker.app.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdUtils {
    private static AdUtils adUtils;
    private static InterstitialAd mInterstitialAd;

    private AdUtils(Context ctx) {
        mInterstitialAd = new InterstitialAd(ctx);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                Log.d(Constants.App_Tag,"closing ad");
                adUtils.loadInterstialAd();
            }
        });
    }

    public static AdUtils getInstance(Context ctx) {
        if(adUtils==null) {
            adUtils=new AdUtils(ctx);
        }
        return adUtils;
    }

    public void loadInterstialAd() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    public void showInterstialAd(Context ctx) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            loadInterstialAd();
            Log.d(Constants.App_Tag, "The interstitial wasn't loaded yet.");
        }

    }
}
