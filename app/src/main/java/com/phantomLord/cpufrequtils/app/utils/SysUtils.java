package com.phantomLord.cpufrequtils.app.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.phantomLord.cpufrequtils.app.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class SysUtils {

    public static String[] getAvailableFrequencies() {
        String[] frequencies = null;
        if (new File(Constants.scaling_available_freq).exists()) {
            frequencies = readOutputFromFile(Constants.scaling_available_freq)
                    .split(" ");
            return frequencies;
        } else if (new File(Constants.time_in_states).exists()) {
            ArrayList<CpuState> states = new ArrayList<CpuState>();
            int i = 0;
            states = TimeInStateReader.TimeInStatesReader().getCpuStateTime(
                    false, false);
            Collections.sort(states);
            frequencies = new String[states.size()];
            for (CpuState object : states) {
                frequencies[i] = String.valueOf(object.getFrequency());
                i++;
            }
            return frequencies;
        } else {
            return new String[]{};
        }
    }

    public static String getCurrentMaxFrequeny() {
        return readOutputFromFile(Constants.scaling_max_freq);
    }

    public static String getCurrentMinFrequency() {
        return readOutputFromFile(Constants.scaling_min_freq);

    }

    public static String[] getAvailableGovernors() {
        return readOutputFromFile(Constants.scaling_available_governors).split(
                " ");
    }

    public static String getCurrentScalingGovernor() {
        return readOutputFromFile(Constants.scaling_governor);
    }

    public static final String[] getAvailableIOScheduler() {
        String schedulerPath = new String();
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
            return new String[]{};
            /*
             * need to return an empty string and not a null because the wheel
			 * widget would just crash if a null value is returned
			 */
        }
        String[] schedulers = readOutputFromFile(schedulerPath).split(" ");
        for (int i = 0; i < schedulers.length; i++) {
            if (schedulers[i].contains("]")) {
                String temp = schedulers[i].substring(1,
                        schedulers[i].length() - 1);
                schedulers[i] = temp;
            }
        }
        return schedulers;
    }

    public static final String getCurrentIOScheduler() {
        String currentScheduler = null;
        String[] schedulers = readOutputFromFile(Constants.available_schedulers)
                .split(" ");
        for (String string : schedulers) {
            if (string.contains("[")) {
                currentScheduler = string;
            }
        }
        if (currentScheduler != null) {
            return currentScheduler.substring(1, currentScheduler.length() - 1);
        } else
            return "";
    }

    public static String getReadAhead() {
        String res = new String();
        for (int i = 0; i < 2; i++) {
            File device = new File(Constants.available_blockdevices + "mmcblk"
                    + i);
            if (device.exists()) {
                device = new File(Constants.available_blockdevices + "mmcblk"
                        + i + "/queue/read_ahead_kb");
                res = readOutputFromFile(device.getAbsolutePath());
            }
        }
        return res;

    }

    public static final void setFrequencyAndGovernor(String maxFrequency,
                                                     String minFrequency, String governor, Context context) {
        int noOfCpus = getCoreCount();
        ArrayList<String> commands = new ArrayList<String>();
        /*
		 * prepare commands for each core
		 */
        if (maxFrequency != null && minFrequency != null)
            for (int i = 0; i < noOfCpus; i++) {
                commands.add("chmod 0644 "
                        + Constants.scaling_governor.replace("cpu0", "cpu" + i)
                        + "\n");
                commands.add("chmod 0664 "
                        + Constants.scaling_min_freq.replace("cpu0", "cpu" + i)
                        + "\n");
                commands.add("chmod 0664 "
                        + Constants.scaling_max_freq.replace("cpu0", "cpu" + i)
                        + "\n");
                commands.add("echo " + governor + " > "
                        + Constants.scaling_governor.replace("cpu0", "cpu" + i)
                        + "\n");
                commands.add("echo " + minFrequency + " > "
                        + Constants.scaling_min_freq.replace("cpu0", "cpu" + i)
                        + "\n");
                commands.add("echo " + maxFrequency.replace("cpu0", "cpu" + i)
                        + " > " + Constants.scaling_max_freq + "\n");

            }

        commands.add("exit" + "\n");
        boolean success = executeRootCommand(commands);
        if (success) {
            String msg = context.getString(R.string.ok_message);
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static int getCoreCount() {
        int cores = 0;
        while (true) {
            File file = new File(Constants.cpufreq_sys_dir.replace("cpu0",
                    "cpu" + cores));
            if (file.exists())
                cores++;
            else
                return cores;
        }
    }

    public static void setDiskSchedulerandReadAhead(String ioScheduler,
                                                    String readAhead, Context ctx) {

        ArrayList<String> mCommands = new ArrayList<String>();
        if (ioScheduler != null) {
            File[] devices = new File(Constants.available_blockdevices)
                    .listFiles();
            for (int i = 0; i < devices.length; i++) {
                String devicePath = devices[i].getAbsolutePath();
                if (!(devicePath.contains("ram") || devicePath.contains("loop") || devicePath
                        .contains("dm"))) {
                    File blockDevice = new File(devices[i].getAbsolutePath()
                            + "/queue/scheduler");
                    if (blockDevice.exists()) {
                        mCommands.add("chmod 0644 "
                                + blockDevice.getAbsolutePath() + "\n");
                        mCommands.add("echo " + ioScheduler + " > "
                                + blockDevice.getAbsolutePath() + " \n ");
                    }
                }
            }
        }
		/*
		 * prepare commands for changing the read ahead cache
		 */
        if (readAhead != null) {
            File block;
            for (int i = 0; i < 2; i++) {
                block = new File(Constants.available_blockdevices + "mmcblk"
                        + i + "/queue/read_ahead_kb");
                if (block.exists()) {
                    mCommands.add("chmod 0644 " + block.getAbsolutePath()
                            + "\n");
                    mCommands.add("echo " + readAhead + " > "
                            + block.getAbsolutePath() + "\n");
                }
            }
            mCommands.add("chmod 0644 " + Constants.SD_CACHE + "\n");
            mCommands.add("echo " + readAhead + " > " + Constants.SD_CACHE
                    + "\n");

        }
        mCommands.add(" exit \n");
        boolean success = executeRootCommand(mCommands);
        if (success) {
            String msg = ctx.getString(R.string.ok_message,
                    getCurrentIOScheduler(), getReadAhead());
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getKernelInfo() {
        String data = readOutputFromFile("/proc/version");
        if (data != null)
            return data;
        return "";
    }

    public static String[] toMhz(String[] values) {
        String[] frequency = new String[values.length];
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                try {
                    frequency[i] = (Integer.parseInt(values[i]) / 1000 + " Mhz");
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    return new String[]{};
					/*
					 * need to return an empty string in case an exception is
					 * thrown , if we return a null value the spinner wheel
					 * widget would just crash and we don't want that do we??
					 */
                }
            }

        }
        return frequency;
    }

    public static boolean isRooted() {
        if (new File("/system/bin/su").exists()
                || new File("/system/xbin/su").exists())
            return true;
        else
            return false;
    }

    public static String readOutputFromFile(String pathToFile) {

        StringBuffer buffer = new StringBuffer();
        String data = null;
        Process process;
        BufferedReader stdinput;
        File file = new File(pathToFile);
        if (!(file.exists())) {
            return "";
        }
        if (file.canRead()) {
            try {
                process = Runtime.getRuntime().exec("cat " + pathToFile);
                stdinput = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));
                while ((data = stdinput.readLine()) != null) {
                    buffer.append(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return buffer.toString();

        }
		/*
		 * try reading the file as root
		 */
        else {
            InputStream inputStream;
            DataOutputStream dos;
            try {
                process = prepareRootShell();
                dos = new DataOutputStream(process.getOutputStream());
                dos.writeBytes("cat " + process);
                dos.flush();
                dos.writeBytes("\n exit ");
                dos.flush();
                dos.close();
                if (process.waitFor() == 0) {
                    inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data = line;
                    }
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return data;
        }

    }

    public static boolean executeRootCommand(ArrayList<String> commands) {
        InputStream is = null;
        DataOutputStream dos;
        try {
            Process mProcess = prepareRootShell();
            dos = new DataOutputStream(mProcess.getOutputStream());
            for (String cmd : commands) {
                dos.writeBytes(cmd);
                dos.flush();
            }
            if (mProcess.waitFor() == 0) {
                return true;
            } else {
                is = mProcess.getErrorStream();
            }
            dos.close();
            if (is != null)
                printOutputOnStdout(is);
            is.close();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void printOutputOnStdout(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                Log.e(Constants.App_Tag, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Process prepareRootShell() throws IOException {
        Process mProcess = Runtime.getRuntime().exec(getSUbinaryPath());
        return mProcess;
    }

    public static String getSUbinaryPath() {
        String path = "/system/bin/su";
        if (new File(path).exists()) {
            return path;
        }
        path = "/system/xbin/su";
        if (new File(path).exists()) {
            return path;
        }
        return null;
    }

    public static String secToString(long tSec) {
        long h = (long) Math.floor(tSec / (60 * 60));
        long m = (long) Math.floor((tSec - h * 60 * 60) / 60);
        long s = tSec % 60;
        String sDur;
        sDur = h + ":";
        if (m < 10)
            sDur += "0";
        sDur += m + ":";
        if (s < 10)
            sDur += "0";
        sDur += s;

        return sDur;

    }
}
