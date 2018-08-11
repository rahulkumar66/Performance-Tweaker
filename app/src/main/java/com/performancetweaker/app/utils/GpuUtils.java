package com.performancetweaker.app.utils;

import com.performancetweaker.app.R;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class GpuUtils {

    public static String getGpuPath() {
        String possiblePath[] = Constants.GPU_PATH;

        for (String s : possiblePath) {
            if (new File(s).exists()) {
                return s;
            }
        }
        return "";
    }

    public static String[] getAvailableGpuFrequencies() {
        String[] possiblePath = Constants.gpu_freqs_avail;
        String gpuPath = getGpuPath();
        String gpuFrequencies[]=new String[]{};

        for (String s : possiblePath) {
            if (new File(gpuPath + s).exists()) {
                gpuFrequencies= SysUtils.readOutputFromFile(gpuPath + s).split(" ");
            }
        }
        for(int i=0;i<gpuFrequencies.length;i++) {
            if(gpuFrequencies[i]=="") {
                return new String[]{};
            }
        }
        return gpuFrequencies;
    }

    public static String[] getAvailableGpuGovernors() {
        String[] possiblePath = Constants.gpu_govs_avail_path;
        String gpu_path = getGpuPath();

        for (String s : possiblePath) {
            if (new File(gpu_path + s).exists()) {
                return SysUtils.readOutputFromFile(gpu_path + s).split(" ");
            }
        }
        return new String[]{};
    }

    public static String getCurrentGpuGovernor() {
        String gpuPath = getGpuPath();
        String governorPath = null;

        for (String path : Constants.gpu_governor_path) {
            if (new File(gpuPath + path).exists()) {
                governorPath = gpuPath + path;
                break;
            }
        }
        if (governorPath != null) {
            return SysUtils.readOutputFromFile(governorPath);
        } else {
            return "";
        }
    }

    public static void setGpuFrequencyScalingGovernor(String governor, Context context) {
        ArrayList<String> commands = new ArrayList<>();
        String gpuPath = getGpuPath();
        String governorPath = null;

        for (String path : Constants.gpu_governor_path) {
            if (new File(gpuPath + path).exists()) {
                governorPath = gpuPath + path;
                break;
            }
        }
        if (governorPath != null) {

            commands.add("chmod 0664 " + governorPath);
            commands.add("echo " + governor + " > " + governorPath);
            boolean success = SysUtils.executeRootCommand(commands);

            if (success) {
                String msg = context.getString(R.string.ok_message);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getMaxGpuFrequency() {
        String possiblePath[] = Constants.gpu_freqs_max;
        String gpu_path = getGpuPath();

        for (String s : possiblePath) {
            if (new File(gpu_path + s).exists()) {
                return SysUtils.readOutputFromFile(gpu_path + s);
            }
        }
        return "";
    }

    public static void setMaxGpuFrequency(String maxFrequency, Context context) {
        ArrayList<String> commands = new ArrayList<>();

        if (maxFrequency != null) {

            String path = getGpuPath();
            String maxFrequencyPath = null;

            for (String s : Constants.gpu_freqs_max) {
                if (new File(path + s).exists()) {
                    maxFrequencyPath = path + s;
                    break;
                }
            }

            commands.add("chmod 0664 " + maxFrequencyPath);
            commands.add("echo " + maxFrequency + " > " + maxFrequencyPath);

            boolean success = SysUtils.executeRootCommand(commands);

            if (success) {
                String msg = context.getString(R.string.ok_message);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getMinGpuFrequency() {
        String possiblePath[] = Constants.gpu_freqs_min;
        String gpu_path = getGpuPath();

        for (String s : possiblePath) {
            if (new File(gpu_path + s).exists()) {
                return SysUtils.readOutputFromFile(gpu_path + s);
            }
        }
        return "";
    }

    public static void setMinFrequency(String minFrequency, Context context) {
        ArrayList<String> commands = new ArrayList<>();

        if (minFrequency != null) {

            String path = getGpuPath();
            String minFrequencyPath = null;

            for (String s : Constants.gpu_freqs_min) {
                if (new File(path + s).exists()) {
                    minFrequencyPath = path + s;
                }
            }

            commands.add("chmod 0664 " + minFrequencyPath);
            commands.add("echo " + minFrequency + " > " + minFrequencyPath);
            boolean success = SysUtils.executeRootCommand(commands);

            if (success) {
                String msg = context.getString(R.string.ok_message);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isGpuFrequencyScalingSupported() {
        String possiblePath[] = Constants.GPU_PATH;
        for (String s : possiblePath) {
            if (new File(s).exists()) {
                return true;
            }
        }
        return false;
    }

    public static String[] toMhz(String... values) {
        String[] frequency = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            try {
                frequency[i] = (Integer.parseInt(values[i].trim()) / (1000 * 1000)) + " Mhz";
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        return frequency;
    }
}
