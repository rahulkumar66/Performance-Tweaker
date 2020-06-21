package com.performancetweaker.app.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CpuFrequencyUtils {

    // CPU
    private static final String CPU_PRESENT = "/sys/devices/system/cpu/present";
    private static final String CPUFREQ_SYS_DIR = "/sys/devices/system/cpu/cpu%d/cpufreq/";
    private static final String SCALING_MIN_FREQ = CPUFREQ_SYS_DIR + "scaling_min_freq";
    private static final String cpuinfo_min_freq = CPUFREQ_SYS_DIR + "cpuinfo_min_freq";
    private static final String SCALING_MAX_FREQ = CPUFREQ_SYS_DIR + "scaling_max_freq";
    private static final String cpuinfo_max_freq = CPUFREQ_SYS_DIR + "cpuinfo_max_freq";
    private static final String scaling_cur_freq = CPUFREQ_SYS_DIR + "scaling_cur_freq";
    private static final String cpuinfo_cur_freq = CPUFREQ_SYS_DIR + "cpuinfo_cur_freq";
    private static final String SCALING_GOVERNOR = CPUFREQ_SYS_DIR + "scaling_governor";
    private static final String SCALING_FREQS_PATH = CPUFREQ_SYS_DIR + "scaling_available_frequencies";
    private static final String SCALING_AVAILABLE_GOVERNORS = CPUFREQ_SYS_DIR + "scaling_available_governors";
    private static final String TIME_IN_STATE_PATH = "/sys/devices/system/cpu/cpu%d/cpufreq/stats/time_in_state";
    public static final String TIME_IN_STATE_2 = "/sys/devices/system/cpu/cpufreq/stats/cpu%d/time_in_state";


    private static final String OPP_TABLE = "/sys/devices/system/cpu/cpu%d/opp_table";

    private static String governor_prop_dir = "/sys/devices/system/cpu/cpufreq/";

    public static String[] getAvailableFrequencies(int cpu) {
        String[] frequencies;
        String currentCpuFreqsPath = SCALING_FREQS_PATH.replace("%d", String.valueOf(cpu));
        String timeInStatePath = TIME_IN_STATE_PATH.replace("%d", String.valueOf(cpu));
        String timeInStatePath2 = TIME_IN_STATE_2.replace("%d", String.valueOf(cpu));
        if (new File(timeInStatePath).exists() || new File(timeInStatePath2).exists()) {
            ArrayList<CpuState> states;
            int i = 0;
            states = TimeInStateReader.TimeInStatesReader().getCpuStateTime(false, false, 0);
            Collections.sort(states);
            frequencies = new String[states.size()];
            for (CpuState object : states) {
                frequencies[i] = String.valueOf(object.getFrequency());
                i++;
            }
            return frequencies;
        } else if (new File(currentCpuFreqsPath).exists()) {
            frequencies = SysUtils.readOutputFromFile(currentCpuFreqsPath).split(" ");
            return frequencies;
        } else {
            return new String[]{};
        }
    }

    public static String getCurrentMaxFrequency(int cpu) {
        String cpuScalingMaxFreqPath = SCALING_MAX_FREQ.replace("%d", String.valueOf(cpu));
        return SysUtils.readOutputFromFile(cpuScalingMaxFreqPath);
    }

    public static String getCurrentMinFrequency(int cpu) {
        String cpuScalingMinFreqPath = SCALING_MIN_FREQ.replace("%d", String.valueOf(cpu));
        return SysUtils.readOutputFromFile(cpuScalingMinFreqPath);
    }

    public static String[] getAvailableGovernors(int cpu) {
        String cpuScalingGovernors = SCALING_AVAILABLE_GOVERNORS.replace("%d", String.valueOf(cpu));
        return SysUtils.readOutputFromFile(cpuScalingGovernors).split(" ");
    }

    public static String getCurrentScalingGovernor(int cpu) {
        String cpuCurrentGov = SCALING_GOVERNOR.replace("%d", String.valueOf(cpu));
        return SysUtils.readOutputFromFile(cpuCurrentGov);
    }

    public static boolean setMinFrequency(String minFrequency) {
        ArrayList<String> commands = new ArrayList<>();
        /*
         * prepare commands for each core
         */
        if (minFrequency != null) {
            for (int i = 0; i < getCpuCount(); i++) {
                commands.add("chmod 0664 " + SCALING_MIN_FREQ.replace("%d", String.valueOf(i)));
                commands.add("echo "
                        + minFrequency
                        + " > "
                        + SCALING_MIN_FREQ.replace("%d", String.valueOf(i)));
            }
            return SysUtils.executeRootCommand(commands);
        }
        return false;
    }

    public static void setMaxFrequency(String maxFrequency) {
        ArrayList<String> commands = new ArrayList<>();
        /*
         * prepare commands for each core
         */
        if (maxFrequency != null) {
            for (int i = 0; i < getCpuCount(); i++) {
                commands.add("chmod 0664 " + SCALING_MAX_FREQ.replace("%d", String.valueOf(i)));
                commands.add("echo "
                        + maxFrequency.replace("%d", String.valueOf(i))
                        + " > "
                        + SCALING_MAX_FREQ);
            }

            boolean success = SysUtils.executeRootCommand(commands);
        }
    }

    public static boolean setGovernor(String governor) {
        ArrayList<String> commands = new ArrayList<>();
        /*
         * prepare commands for each core
         */
        if (governor != null) {
            for (int i = 0; i < getCpuCount(); i++) {
                commands.add("chmod 0644 " + SCALING_GOVERNOR.replace("%d", String.valueOf(i)));

                commands.add("echo "
                        + governor
                        + " > "
                        + SCALING_GOVERNOR.replace("%d", String.valueOf(i)));
            }
            return SysUtils.executeRootCommand(commands);
        }
        return false;
    }

    public static int getCpuCount() {
        String output = SysUtils.readOutputFromFile(CPU_PRESENT);
        int cpuCount = output.equals("0") ? 1 : Integer.parseInt(output.split("-")[1]) + 1;
        if (cpuCount == 0) {
            cpuCount = Runtime.getRuntime().availableProcessors();
        }
        return cpuCount;
    }

    public static boolean isBigLITTLE() {
        if (getCpuCount() > 4) {
            return true;
        }
        return false;
    }

    public static GovernorProperty[] getGovernorProperties() {
        GovernorProperty[] governorProperties = null;
        String filePath = governor_prop_dir + getCurrentScalingGovernor(0);
        File f = new File(filePath);

        if (f.exists()) {

            List<File> govProperties;
            File[] files = f.listFiles();
            if (files == null) {
                //try reading as root
                govProperties = getGovernerPropAsRoot(filePath);
            } else {
                govProperties = Arrays.asList(files);
            }

            if (govProperties.size() != 0) {
                governorProperties = new GovernorProperty[govProperties.size()];

                for (int i = 0; i < governorProperties.length; i++) {
                    governorProperties[i] = new GovernorProperty(govProperties.get(i).getName(),
                            SysUtils.readOutputFromFile(govProperties.get(i).getAbsolutePath()));
                }
            }
        }
        return governorProperties;
    }

    public static ArrayList<File> getGovernerPropAsRoot(String path) {
        DataOutputStream dos;
        InputStream is;
        ArrayList<File> files = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("su");
            if (process != null) {
                dos = new DataOutputStream(process.getOutputStream());
                dos.writeBytes("ls " + path + "\n");
                dos.writeBytes("exit \n");
                dos.flush();
                dos.close();
                if (process.waitFor() == 0) {
                    is = process.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        files.add(new File(path + "/" + line));
                    }
                } else {
                    is = process.getErrorStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) Log.d("error", line);
                }
            }

        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static boolean setGovernorProperty(GovernorProperty property) {
        String path = governor_prop_dir
                + getCurrentScalingGovernor(0)
                + "/"
                + property.getGovernorProperty();
        ArrayList<String> commands = new ArrayList<>();

        commands.add("chmod 0644 " + path);
        commands.add("echo " + property.getGovernorPropertyValue() + " > " + path);

        return SysUtils.executeRootCommand(commands);
    }

    public static String[] toMhz(String... values) {
        String[] frequency = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            try {
                frequency[i] = (Integer.parseInt(values[i].trim()) / 1000) + " Mhz";
            } catch (NumberFormatException | NullPointerException exception) {
                exception.printStackTrace();
            }
        }
        return frequency;
    }
}
