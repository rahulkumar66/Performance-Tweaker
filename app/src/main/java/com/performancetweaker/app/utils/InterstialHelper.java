package com.performancetweaker.app.utils;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

public class InterstialHelper {

    private static InterstitialAd fanInterstitial;
    private static InterstialHelper interstialHelper;
    private static com.google.android.gms.ads.InterstitialAd adMobInterstitial;
    private static boolean showAdsFlag = false;
    private static Integer adShowCounter = 0;

    private static String TAG = Constants.App_Tag;

    private InterstialHelper(Context context, boolean showAds) {
        showAdsFlag = showAds;
        if (showAdsFlag) {
            fanInterstitial = new InterstitialAd(context, Constants.FAN_INTERSTITIAL_ID);
            InterstitialAdListener adListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    Log.i(TAG, "FAN Interstitial ad displayed.");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    Log.i(TAG, "FAN Interstitial ad dismissed.");
                    if (!fanInterstitial.isAdLoaded() || fanInterstitial.isAdInvalidated()) {
                        fanInterstitial.loadAd();
                    }
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
            };
            fanInterstitial.buildLoadAdConfig().withAdListener(adListener).build();
            fanInterstitial.loadAd();

            adMobInterstitial = new com.google.android.gms.ads.InterstitialAd(context);
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
//
//    public void loadAd() {
//        if (showAdsFlag) {
//            if (!adMobInterstitial.isLoaded()) {
//                adMobInterstitial.loadAd(new AdRequest.Builder().build());
//            }
//            if (!fanInterstitial.isAdLoaded() || fanInterstitial.isAdInvalidated()) {
//                fanInterstitial.loadAd();
//            }
//        }
//    }

    public void showAd() {
        if (showAdsFlag && adShowCounter % 3 == 0) {
            if (adMobInterstitial.isLoaded()) {
                adMobInterstitial.show();
            } else if (fanInterstitial.isAdLoaded()) {
                fanInterstitial.show();
            }
        }
        adShowCounter++;
    }

    public void destroyAd() {
        if (fanInterstitial != null) {
            fanInterstitial.destroy();
        }
    }
}
