package com.performancetweaker.app.utils;

import java.io.File;
import java.util.ArrayList;

public class IOUtils {
    private static String AVAILABLE_BLOCKDEVICES = "/sys/block/";
    private static String AVAILABLE_SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";
    private static String AVAILABLE_SCHEDULER_PATH = "/sys/block/mmcblk1/queue/scheduler";

    public static String[] getAvailableIOScheduler() {
        String schedulerPath = getSchedulerPath();
        if (schedulerPath != null) {
            String[] schedulers = SysUtils.readOutputFromFile(schedulerPath).split(" ");
            for (int i = 0; i < schedulers.length; i++) {
                if (schedulers[i].contains("]")) {
                    String temp = schedulers[i].substring(1, schedulers[i].length() - 1);
                    schedulers[i] = temp;
                }
            }
            return schedulers;
        }
        return new String[0];
    }

    public static String getCurrentIOScheduler() {
        String currentScheduler = null;
        String schedulerPath = getSchedulerPath();
        if (schedulerPath != null) {
            String[] schedulers = SysUtils.readOutputFromFile(AVAILABLE_SCHEDULER).split(" ");
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
        } else {
            return "";
        }
    }

    public static String getReadAhead() {
        String res = null;

        for (int i = 0; i < 2; i++) {
            File device = new File(AVAILABLE_BLOCKDEVICES + "mmcblk" + i);
            if (device.exists()) {

                device = new File(AVAILABLE_BLOCKDEVICES + "mmcblk" + i + "/queue/read_ahead_kb");
                res = SysUtils.readOutputFromFile(device.getAbsolutePath());
            }
        }
        return res;
    }

    public static boolean setDiskScheduler(String ioScheduler) {
        ArrayList<String> mCommands = new ArrayList<>();
        if (ioScheduler != null) {
            File[] devices = new File(AVAILABLE_BLOCKDEVICES).listFiles();

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
            return SysUtils.executeRootCommand(mCommands);
        }
        return false;
    }

    public static boolean setReadAhead(String readAhead) {
        ArrayList<String> mCommands = new ArrayList<>();
        /*
         * prepare commands for changing the read ahead cache
         */
        if (readAhead != null) {
            File block;
            for (int i = 0; i < 2; i++) {
                block = new File(AVAILABLE_BLOCKDEVICES + "mmcblk" + i + "/queue/read_ahead_kb");
                if (block.exists()) {
                    mCommands.add("chmod 0644 " + block.getAbsolutePath());
                    mCommands.add("echo " + readAhead + " > " + block.getAbsolutePath());
                }
            }
            mCommands.add("chmod 0644 " + Constants.SD_CACHE);
            mCommands.add("echo " + readAhead + " > " + Constants.SD_CACHE);
        }
        return SysUtils.executeRootCommand(mCommands);
    }

    private static String getSchedulerPath() {
        if (new File(AVAILABLE_SCHEDULER).exists()) {
            return AVAILABLE_SCHEDULER;
        } else if (new File(AVAILABLE_SCHEDULER_PATH).exists()) {
            return AVAILABLE_SCHEDULER_PATH;
        } else if (new File(Constants.ioscheduler_mtd).exists()) {
            /*
             * Some devices don't have mmcblk0 block device so we instead use
             * mtdblock0 to read the available schedulers
             */
            return Constants.ioscheduler_mtd;
        } else {
            return null;
        }
    }
}
