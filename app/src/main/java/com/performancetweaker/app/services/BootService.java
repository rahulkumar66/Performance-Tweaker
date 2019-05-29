package com.performancetweaker.app.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.Constants;
import com.performancetweaker.app.utils.CpuFrequencyUtils;
import com.performancetweaker.app.utils.GpuUtils;
import com.performancetweaker.app.utils.IOUtils;
import com.stericson.RootTools.RootTools;

import java.util.Set;

public class BootService extends IntentService {

    SharedPreferences prefs;
    Context context;
    static final String CHANNEL_ID = "onbootNotificationChannel";
    static final int ON_BOOT_NOTIFICATION_ID = 1;

    public BootService() {
        super("Boot Service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    getString(R.string.apply_on_boot_notification), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);

            Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID);
            builder.setContentTitle(getString(R.string.apply_on_boot_notification))
                    .setSmallIcon(R.mipmap.ic_launcher);
            startForeground(ON_BOOT_NOTIFICATION_ID, builder.build());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            Thread.sleep(30000);// wait some time and the apply settings
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
                    GpuUtils gpuUtils = GpuUtils.getInstance();
                    if (max != null)
                        gpuUtils.setMaxGpuFrequency(max, context);
                    if (min != null)
                        gpuUtils.setMinFrequency(min, context);
                    if (gov != null)
                        gpuUtils.setGpuFrequencyScalingGovernor(gov, context);
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
            }
        }
        stopSelf();
        return START_NOT_STICKY;
    }
}
