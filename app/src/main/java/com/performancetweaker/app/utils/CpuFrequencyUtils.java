package com.performancetweaker.app.utils;

import com.performancetweaker.app.R;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

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

    public static void setMinFrequency(String minFrequency, Context context) {
        ArrayList<String> commands = new ArrayList<>();
        /*
         * prepare commands for each core
		 */
        if (minFrequency != null) {
            for (int i = 0; i < getCoreCount(); i++) {
                commands.add("chmod 0664 " + Constants.scaling_min_freq.replace("cpu0", "cpu" + i) + "\n");
                commands.add("echo "
                        + minFrequency
                        + " > "
                        + Constants.scaling_min_freq.replace("cpu0", "cpu" + i)
                        + "\n");
            }

            commands.add("exit" + "\n");
            boolean success = SysUtils.executeRootCommand(commands);
            if (success) {
                String msg = context.getString(R.string.ok_message);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void setMaxFrequency(String maxFrequency, Context context) {
        ArrayList<String> commands = new ArrayList<>();
        /*
         * prepare commands for each core
		 */
        if (maxFrequency != null) {
            for (int i = 0; i < getCoreCount(); i++) {
                commands.add("chmod 0664 " + Constants.scaling_max_freq.replace("cpu0", "cpu" + i) + "\n");
                commands.add("echo "
                        + maxFrequency.replace("cpu0", "cpu" + i)
                        + " > "
                        + Constants.scaling_max_freq
                        + "\n");
            }

            commands.add("exit" + "\n");
            boolean success = SysUtils.executeRootCommand(commands);
            if (success) {
                String msg = context.getString(R.string.ok_message);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void setGovernor(String governor, Context context) {

        ArrayList<String> commands = new ArrayList<>();
        /*
         * prepare commands for each core
		 */
        if (governor != null) {
            for (int i = 0; i < getCoreCount(); i++) {
                commands.add("chmod 0644 " + Constants.scaling_governor.replace("cpu0", "cpu" + i) + "\n");

                commands.add("echo "
                        + governor
                        + " > "
                        + Constants.scaling_governor.replace("cpu0", "cpu" + i)
                        + "\n");
            }
            commands.add("exit" + "\n");

            boolean success = SysUtils.executeRootCommand(commands);
            if (success) {
                String msg = context.getString(R.string.governor_applied);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
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
        File f = new File(Constants.governor_prop_dir + getCurrentScalingGovernor());

        if (f.exists()) {
            File[] govProperties = f.listFiles();

            if (govProperties != null && govProperties.length != 0) {
                governorProperties = new GovernorProperty[govProperties.length];

                for (int i = 0; i < governorProperties.length; i++) {
                    governorProperties[i] = new GovernorProperty(govProperties[i].getName(),
                            SysUtils.readOutputFromFile(govProperties[i].getAbsolutePath()));
                }
            }
        }
        return governorProperties;
    }

    public static void setGovernorProperty(GovernorProperty property, Context context) {
        String path = Constants.governor_prop_dir
                + getCurrentScalingGovernor()
                + "/"
                + property.getGovernorProperty();
        ArrayList<String> commands = new ArrayList<>();

        commands.add("chmod 0644 " + path + "\n");
        commands.add("echo " + property.getGovernorPropertyValue() + " > " + path + "\n");
        commands.add("exit" + "\n");

        if (SysUtils.executeRootCommand(commands)) {
            String msg = context.getString(R.string.governor_applied);
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static String[] toMhz(String... values) {
        String[] frequency = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            try {
                frequency[i] = (Integer.parseInt(values[i]) / 1000 + " Mhz");
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        return frequency;
    }
}
