package com.phantomLord.cpufrequtils.app.utils;

import java.io.File;

/**
 * Created by rahul on 20/6/15.
 */
public class GpuUtils {

    public static String[] getAvailableGpuFrequencies() {
        String[] possiblePath = Constants.gpu_freqs_avail;
        String gpu_path = getGpuPath();

        for (String s : possiblePath) {
            if (new File(gpu_path + s).exists()) {
                return SysUtils.readOutputFromFile(gpu_path + s).split(" ");
            }
        }
        return new String[]{};
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

    public static String getGpuPath() {
        String possiblePath[] = Constants.GPU_PATH;

        for (String s : possiblePath) {
            if (new File(s).exists()) {
                return s;
            }
        }
        return "";
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
}
