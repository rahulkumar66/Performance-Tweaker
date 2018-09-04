package com.performancetweaker.app.utils;

import com.performancetweaker.app.R;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class GpuUtils {

    private static GpuUtils gpuUtils;

    public static GpuUtils getInstance() {
        if (gpuUtils == null) {
            gpuUtils = new GpuUtils();
        }
        return gpuUtils;
    }

    private GpuUtils() {
    }

    private String[] GPU_GOVS_AVAIL_PATH = new String[]{
            "/sys/class/kgsl/kgsl-3d0/devfreq/available_governors",
            "/sys/devices/platform/omap/pvrsrvkm.0/sgxfreq/governor_list",
            "/sys/devices/platform/dfrgx/devfreq/dfrgx/available_governors"
    };

    private String[] GPU_FREQS_AVAIL = new String[]{
            "/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/gpu_available_frequencies",
            "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/gpu_available_frequencies",
            "/sys/class/kgsl/kgsl-3d0/gpu_available_frequencies",
            "/sys/devices/platform/dfrgx/devfreq/dfrgx/available_frequencies",
            "/sys/devices/platform/omap/pvrsrvkm.0/sgxfreq/frequency_list"
    };

    private String[] GPU_FREQS_MIN = new String[]{
            "/sys/class/kgsl/kgsl-3d0/devfreq/min_freq",
            "/sys/kernel/tegra_gpu/gpu_floor_rate",
            "/sys/devices/platform/dfrgx/devfreq/dfrgx/min_freq"
    };

    private String[] GPU_FREQS_MAX = new String[]{
            "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk",
            "/sys/class/kgsl/kgsl-3d0/max_gpuclk",
            "/sys/devices/platform/omap/pvrsrvkm.0/sgxfreq/frequency_limit",
            "/sys/kernel/tegra_gpu/gpu_cap_rate",
            "/sys/devices/platform/dfrgx/devfreq/dfrgx/max_freq"
    };

    private String[] GPU_GOVERNOR_PATH = new String[]{
            "/sys/class/kgsl/kgsl-3d0/pwrscale/trustzone/governor",
            "/sys/class/kgsl/kgsl-3d0/devfreq/governor",
            "/sys/devices/platform/omap/pvrsrvkm.0/sgxfreq/governor",
            "/sys/devices/platform/dfrgx/devfreq/dfrgx/governor"
    };

    public String[] getAvailableGpuFrequencies() {
        String[] possiblePath = GPU_FREQS_AVAIL;
        String gpuFrequencies[] = new String[]{};

        for (String path : possiblePath) {
            if (new File(path).exists()) {
                gpuFrequencies = SysUtils.readOutputFromFile(path).split(" ");
            }
        }
        for (int i = 0; i < gpuFrequencies.length; i++) {
            if (gpuFrequencies[i] == "") {
                return new String[]{};
            }
        }
        return gpuFrequencies;
    }

    public String[] getAvailableGpuGovernors() {
        String[] possiblePath = GPU_GOVS_AVAIL_PATH;

        for (String s : possiblePath) {
            if (new File(s).exists()) {
                return SysUtils.readOutputFromFile(s).split(" ");
            }
        }
        return new String[]{};
    }

    public String getCurrentGpuGovernor() {
        String governorPath = null;

        for (String path : GPU_GOVERNOR_PATH) {
            if (new File(path).exists()) {
                governorPath = path;
                break;
            }
        }
        if (governorPath != null) {
            return SysUtils.readOutputFromFile(governorPath);
        } else {
            return "";
        }
    }

    public void setGpuFrequencyScalingGovernor(String governor, Context context) {
        ArrayList<String> commands = new ArrayList<>();
        String governorPath = null;

        for (String path : GPU_GOVERNOR_PATH) {
            if (new File(path).exists()) {
                governorPath = path;
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

    public String getMaxGpuFrequency() {
        String possiblePath[] = GPU_FREQS_MAX;

        for (String s : possiblePath) {
            if (new File(s).exists()) {
                return SysUtils.readOutputFromFile(s);
            }
        }
        return "";
    }

    public void setMaxGpuFrequency(String maxFrequency, Context context) {
        ArrayList<String> commands = new ArrayList<>();

        if (maxFrequency != null) {

            String maxFrequencyPath = null;

            for (String s : GPU_FREQS_MAX) {
                if (new File(s).exists()) {
                    maxFrequencyPath = s;
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

    public String getMinGpuFrequency() {
        String possiblePath[] = GPU_FREQS_MIN;

        for (String s : possiblePath) {
            if (new File(s).exists()) {
                return SysUtils.readOutputFromFile(s);
            }
        }
        return "";
    }

    public void setMinFrequency(String minFrequency, Context context) {
        ArrayList<String> commands = new ArrayList<>();

        if (minFrequency != null) {

            String minFrequencyPath = null;

            for (String s : GPU_FREQS_MIN) {
                if (new File(s).exists()) {
                    minFrequencyPath = s;
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

    public boolean isGpuFrequencyScalingSupported() {
        String possiblePath[] = GPU_FREQS_AVAIL;
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
