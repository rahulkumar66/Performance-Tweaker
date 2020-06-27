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

    public static File[] listFiles(String directory) {
        File[] filesList = new File(directory).listFiles();
        if (filesList == null || filesList.length == 0) {
            //try as root
            String output = SysUtils.executeCommandWithOutput(true, "ls " + directory);
            String filesPath[] = output.split("\n");
            filesList = new File[filesPath.length];
            for (int i = 0; i < filesPath.length; i++) {
                filesList[i] = new File(directory + filesPath[i]);
            }
        }
        return filesList;
    }


}
