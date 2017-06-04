package com.performancetweaker.performancetweaker.app.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.performancetweaker.performancetweaker.app.R;
import com.performancetweaker.performancetweaker.app.utils.Constants;
import com.performancetweaker.performancetweaker.app.utils.CpuFrequencyUtils;
import com.performancetweaker.performancetweaker.app.utils.GpuUtils;
import com.performancetweaker.performancetweaker.app.utils.IOUtils;
import com.stericson.RootTools.RootTools;

import java.util.Set;

public class BootService extends IntentService {

    SharedPreferences prefs;
    Context context;

    public BootService() {
        super("Boot Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            Thread.sleep(30000);//wait some time and the apply settings
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (RootTools.isAccessGiven()) {
            Set<String> applyOnBootSet = prefs.getStringSet(getString(R.string.apply_on_boot), null);

            if (applyOnBootSet != null) {

                if (applyOnBootSet.contains(getString(R.string.cpu_frequency))) {
                    String max, min, gov;
                    max = prefs.getString(Constants.PREF_CPU_MAX_FREQ, null);
                    min = prefs.getString(Constants.PREF_CPU_MIN_FREQ, null);
                    gov = prefs.getString(Constants.PREF_CPU_GOV, null);
                    Log.d(Constants.App_Tag, max + " " + min + " " + gov);
                    if (max != null)
                        CpuFrequencyUtils.setMaxFrequency(max, context);
                    if (min != null)
                        CpuFrequencyUtils.setMinFrequency(min, context);
                    if (gov != null)
                        CpuFrequencyUtils.setGovernor(gov, context);

                }
                if (applyOnBootSet.contains(getString(R.string.gpu_frequency))) {
                    String max, min, gov;
                    max = prefs.getString(Constants.PREF_GPU_MAX, null);
                    min = prefs.getString(Constants.PREF_GPU_MIN, null);
                    gov = prefs.getString(Constants.PREF_GPU_GOV, null);
                    Log.d("gpu", max + " " + min + " " + gov);
                    if (max != null)
                        GpuUtils.setMaxGpuFrequency(max, context);
                    if (min != null)
                        GpuUtils.setMinFrequency(min, context);
                    if (gov != null)
                        GpuUtils.setGpuFrequencyScalingGovernor(gov, context);
                }
                if (applyOnBootSet.contains(getString(R.string.vm))) {

                }
                if (applyOnBootSet.contains(getString(R.string.io))) {
                    String ioScheduler, readAhead;
                    ioScheduler = prefs.getString(Constants.PREF_IO_SCHEDULER, null);
                    readAhead = prefs.getString(Constants.PREF_READ_AHEAD, null);
                    Log.d("io", ioScheduler + " " + readAhead);

                    if (ioScheduler != null)
                        IOUtils.setDiskScheduler(ioScheduler, context);
                    if (readAhead != null)
                        IOUtils.setReadAhead(readAhead, context);
                }
                if (applyOnBootSet.contains(getString(R.string.build_prop))) {

                }

                //Log.d("he");
         /*   if (prefs.getBoolean(Constants.PREF_CPU_APPLY_ON_BOOT, false)) {
                String max, min, gov;
                max = prefs.getString(Constants.PREF_MAX_FREQ, null);
                min = prefs.getString(Constants.PREF_MIN_FREQ, null);
                gov = prefs.getString(Constants.PREF_GOV, null);

                if (max != null || min != null || gov != null) {

                }
            }
            if (prefs.getBoolean(Constants.PREF_IO_APPLY_ON_BOOT, false)) {
                String ioscheduler = prefs.getString(Constants.PREF_IO_SCHEDULER, null);
                String readAhead = prefs.getString(Constants.PREF_READ_AHEAD, null);

                if (ioscheduler != null || readAhead != null) {
                    //   IOUtils.setDiskSchedulerandReadAhead(ioscheduler,
                    //         readAhead, context);
                }
            }
            */
            }
        }

        stopSelf();
    }
}
