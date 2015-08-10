package com.phantomLord.cpufrequtils.app.utils;

import java.io.File;

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

    public static String[] toMhz(String[] values) {

        String[] frequency = new String[values.length];

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                try {
                    frequency[i] = (Integer.parseInt(values[i]) / (1000 * 1000) + " Mhz");
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
            }
        }
        return frequency;
    }

}
