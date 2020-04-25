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

    public static String[] getAvailableFrequencies() {
        String[] frequencies;
        if (new File(Constants.scaling_available_freq).exists()) {
            frequencies = SysUtils.readOutputFromFile(Constants.scaling_available_freq).split(" ");
            return frequencies;
        } else if (new File(Constants.time_in_states).exists()) {
            ArrayList<CpuState> states;
            int i = 0;
            states = TimeInStateReader.TimeInStatesReader().getCpuStateTime(false, false);
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

    public static String getCurrentMaxFrequency() {
        return SysUtils.readOutputFromFile(Constants.scaling_max_freq);
    }

    public static String getCurrentMinFrequency() {
        return SysUtils.readOutputFromFile(Constants.scaling_min_freq);
    }

    public static String[] getAvailableGovernors() {
        return SysUtils.readOutputFromFile(Constants.scaling_available_governors).split(" ");
    }

    public static String getCurrentScalingGovernor() {
        return SysUtils.readOutputFromFile(Constants.scaling_governor);
    }

    public static boolean setMinFrequency(String minFrequency) {
        ArrayList<String> commands = new ArrayList<>();
        /*
         * prepare commands for each core
		 */
        if (minFrequency != null) {
            for (int i = 0; i < getCoreCount(); i++) {
                commands.add("chmod 0664 " + Constants.scaling_min_freq.replace("cpu0", "cpu" + i));
                commands.add("echo "
                        + minFrequency
                        + " > "
                        + Constants.scaling_min_freq.replace("cpu0", "cpu" + i));
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
            for (int i = 0; i < getCoreCount(); i++) {
                commands.add("chmod 0664 " + Constants.scaling_max_freq.replace("cpu0", "cpu" + i));
                commands.add("echo "
                        + maxFrequency.replace("cpu0", "cpu" + i)
                        + " > "
                        + Constants.scaling_max_freq);
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
            for (int i = 0; i < getCoreCount(); i++) {
                commands.add("chmod 0644 " + Constants.scaling_governor.replace("cpu0", "cpu" + i));

                commands.add("echo "
                        + governor
                        + " > "
                        + Constants.scaling_governor.replace("cpu0", "cpu" + i));
            }
           return SysUtils.executeRootCommand(commands);
        }
        return false;
    }

    public static int getCoreCount() {
        int cores = 0;
        while (true) {
            File file = new File(Constants.cpufreq_sys_dir.replace("cpu0", "cpu" + cores));
            if (file.exists()) {
                cores++;
            } else {
                return cores;
            }
        }
    }

    public static GovernorProperty[] getGovernorProperties() {
        GovernorProperty[] governorProperties = null;
        String filePath = Constants.governor_prop_dir + getCurrentScalingGovernor();
        File f = new File(filePath);

        if (f.exists()) {

            List<File> govProperties = new ArrayList<>();
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
        String path = Constants.governor_prop_dir
                + getCurrentScalingGovernor()
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
