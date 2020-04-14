package com.performancetweaker.app.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

public class IOUtils {

    public static String[] getAvailableIOScheduler() {
        String schedulerPath;
        if (new File(Constants.available_schedulers).exists()) {
            schedulerPath = Constants.available_schedulers;
        } else if (new File(Constants.available_schedulers_path).exists()) {
            schedulerPath = Constants.available_schedulers_path;
            /*
             * Some devices don't have mmcblk0 block device so we instead use
             * mtdblock0 to read the available schedulers
             */
        } else if (new File(Constants.ioscheduler_mtd).exists()) {
            schedulerPath = Constants.ioscheduler_mtd;
        } else {
            return null;
        }
        String[] schedulers = SysUtils.readOutputFromFile(schedulerPath).split(" ");
        for (int i = 0; i < schedulers.length; i++) {
            if (schedulers[i].contains("]")) {
                String temp = schedulers[i].substring(1, schedulers[i].length() - 1);
                schedulers[i] = temp;
            }
        }
        return schedulers;
    }

    public static String getCurrentIOScheduler() {
        String currentScheduler = null;
        String[] schedulers = SysUtils.readOutputFromFile(Constants.available_schedulers).split(" ");
        for (String string : schedulers) {
            if (string.contains("[")) {
                currentScheduler = string;
            }
        }
        if (currentScheduler != null) {
            return currentScheduler.substring(1, currentScheduler.length() - 1);
        } else {
            return "";
        }
    }

    public static String getReadAhead() {

        String res = null;

        for (int i = 0; i < 2; i++) {

            File device = new File(Constants.available_blockdevices + "mmcblk" + i);
            if (device.exists()) {

                device = new File(Constants.available_blockdevices + "mmcblk" + i + "/queue/read_ahead_kb");
                res = SysUtils.readOutputFromFile(device.getAbsolutePath());
            }
        }
        return res;
    }

    public static void setDiskScheduler(String ioScheduler, Context context) {

        ArrayList<String> mCommands = new ArrayList<>();

        if (ioScheduler != null) {

            File[] devices = new File(Constants.available_blockdevices).listFiles();

            if (devices != null) {
                for (int i = 0; i < devices.length; i++) {

                    String devicePath = devices[i].getAbsolutePath();

                    if (!(devicePath.contains("ram") || devicePath.contains("loop") || devicePath.contains(
                            "dm"))) {

                        File blockDevice = new File(devices[i].getAbsolutePath() + "/queue/scheduler");

                        if (blockDevice.exists()) {
                            mCommands.add("chmod 0644 " + blockDevice.getAbsolutePath());
                            mCommands.add("echo " + ioScheduler + " > " + blockDevice.getAbsolutePath());
                        }
                    }
                }
            }

            boolean success = SysUtils.executeRootCommand(mCommands);
        }
    }

    public static void setReadAhead(String readAhead, Context ctx) {

        ArrayList<String> mCommands = new ArrayList<>();
        /*
         * prepare commands for changing the read ahead cache
         */
        if (readAhead != null) {
            File block;
            for (int i = 0; i < 2; i++) {
                block = new File(Constants.available_blockdevices + "mmcblk" + i + "/queue/read_ahead_kb");
                if (block.exists()) {
                    mCommands.add("chmod 0644 " + block.getAbsolutePath());
                    mCommands.add("echo " + readAhead + " > " + block.getAbsolutePath());
                }
            }
            mCommands.add("chmod 0644 " + Constants.SD_CACHE);
            mCommands.add("echo " + readAhead + " > " + Constants.SD_CACHE);
        }
        boolean success = SysUtils.executeRootCommand(mCommands);
    }
}
