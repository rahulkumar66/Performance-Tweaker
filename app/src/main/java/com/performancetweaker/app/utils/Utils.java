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

    public static boolean fileExists(String filePath) {
        if (new File(filePath).exists()) {
            return true;
        } else {
            String output = SysUtils.executeCommandWithOutput(true, "[ -e " + filePath + " ] && echo true");
            return output != null && output.equals("true");
        }
    }

    public static String strFormat(String text, Object... format) {
        return String.format(text, format);
    }

    public static int strToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
