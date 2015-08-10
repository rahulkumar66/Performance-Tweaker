package com.phantomLord.cpufrequtils.app.utils;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.phantomLord.cpufrequtils.app.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class CpuFrequencyUtils {

    public static String[] getAvailableFrequencies() {
        String[] frequencies;
        if (new File(Constants.scaling_available_freq).exists()) {
            frequencies = SysUtils.readOutputFromFile(Constants.scaling_available_freq)
                    .split(" ");
            return frequencies;
        } else if (new File(Constants.time_in_states).exists()) {
            ArrayList<CpuState> states;
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
        return SysUtils.readOutputFromFile(Constants.scaling_max_freq);
    }

    public static String getCurrentMinFrequency() {
        return SysUtils.readOutputFromFile(Constants.scaling_min_freq);

    }

    public static String[] getAvailableGovernors() {
        return SysUtils.readOutputFromFile(Constants.scaling_available_governors).split(
                " ");
    }

    public static String getCurrentScalingGovernor() {
        return SysUtils.readOutputFromFile(Constants.scaling_governor);
    }

    public static final void setMinFrequency(String minFrequency, Context context) {
        getCoreCount();
        ArrayList<String> commands = new ArrayList<>();
        /*
         * prepare commands for each core
		 */
        if (minFrequency != null) {
            for (int i = 0; i < getCoreCount(); i++) {
                commands.add("chmod 0664 "
                        + Constants.scaling_min_freq.replace("cpu0", "cpu" + i) + "\n");
                commands.add("echo " + minFrequency + " > "
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


    public static final void setMaxFrequency(String maxFrequency, Context context) {
        getCoreCount();
        Log.d("status", getCoreCount() + "");
        ArrayList<String> commands = new ArrayList<String>();
        /*
         * prepare commands for each core
		 */
        if (maxFrequency != null) {
            for (int i = 0; i < getCoreCount(); i++) {
                commands.add("chmod 0664 "
                        + Constants.scaling_max_freq.replace("cpu0", "cpu" + i)
                        + "\n");
                commands.add("echo " + maxFrequency.replace("cpu0", "cpu" + i)
                        + " > " + Constants.scaling_max_freq + "\n");
            }

            commands.add("exit" + "\n");
            boolean success = SysUtils.executeRootCommand(commands);
            if (success) {
                String msg = context.getString(R.string.ok_message);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static final void setGovernor(String governor, Context context) {
        getCoreCount();
        ArrayList<String> commands = new ArrayList<String>();
        /*
         * prepare commands for each core
		 */
        if (governor != null) {
            for (int i = 0; i < getCoreCount(); i++) {
                commands.add("chmod 0644 "
                        + Constants.scaling_governor.replace("cpu0", "cpu" + i)
                        + "\n");

                commands.add("echo " + governor + " > "
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
            File file = new File(Constants.cpufreq_sys_dir.replace("cpu0",
                    "cpu" + cores));
            if (file.exists())
                cores++;
            else
                return cores;
        }
    }

    public static GovernorProperties[] getGovernorProperties() {
        GovernorProperties[] governorProperties = null;
        File f = new File(Constants.governor_prop_dir + getCurrentScalingGovernor());

        if (f.exists()) {
            File[] govProperties = f.listFiles();

            if (govProperties != null && govProperties.length != 0) {
                governorProperties = new GovernorProperties[govProperties.length];

                for (int i = 0; i < governorProperties.length; i++) {
                    governorProperties[i] = new GovernorProperties();
                    governorProperties[i].setGovernorProperty(govProperties[i].getName());
                    governorProperties[i].setGovernorPropertyValue(SysUtils.
                            readOutputFromFile(govProperties[i].getAbsolutePath()));
                }
            }
        }
        return governorProperties;
    }

    public static void setGovernorProperty(GovernorProperties property) {
        String path = Constants.governor_prop_dir + "/" + getCurrentScalingGovernor()
                + property.getGovernorProperty();
        //TODO
    }

    public static String[] toMhz(String[] values) {

        String[] frequency = new String[values.length];

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                try {
                    frequency[i] = (Integer.parseInt(values[i]) / 1000 + " Mhz");
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
            }
        }
        return frequency;
    }

}
