package com.performancetweaker.app.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import java.io.File;

public class Utils {

    public static void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static boolean fileExists(String file) {
        return new File(file).exists();
    }
}
