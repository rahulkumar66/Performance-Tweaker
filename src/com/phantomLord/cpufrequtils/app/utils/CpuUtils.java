package com.phantomLord.cpufrequtils.app.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class CpuUtils implements Constants {

	public static String[] getAvailableFrequencies() {
		String[] frequencies = null;
		if (new File(scaling_available_freq).exists()) {
			frequencies = RootUtils.readOutputFromFile(scaling_available_freq)
					.split(" ");
			return frequencies;
		} else if (new File(time_in_states).exists()) {
			ArrayList<CpuState> states = new ArrayList<CpuState>();
			int i = 0;
			states = new TimeInStateReader(true).getCpuStateTime(false);
			Collections.sort(states);
			frequencies = new String[states.size()];
			for (CpuState object : states) {
				frequencies[i] = String.valueOf(object.getFrequency());
				i++;
			}
			return frequencies;
		} else {
			return new String[] {};
		}
	}

	public static String getCurrentMaxFrequeny() {
		return RootUtils.readOutputFromFile(scaling_max_freq);
	}

	public static String getCurrentMinFrequency() {
		return RootUtils.readOutputFromFile(scaling_min_freq);

	}

	public static String[] getAvailableGovernors() {
		return RootUtils.readOutputFromFile(scaling_available_governors).split(
				" ");
	}

	public static String getCurrentScalingGovernor() {
		return RootUtils.readOutputFromFile(scaling_governor);
	}

	public static final String[] getAvailableIOScheduler() {
		String schedulerPath = new String();
		if (new File(available_schedulers).exists()) {
			schedulerPath = available_schedulers;

		} else if (new File(available_schedulers_path).exists()) {
			schedulerPath = available_schedulers_path;
			/*
			 * Some devices don't have mmcblk0 block device so we instead use
			 * mtdblock0 to read the available schedulers
			 */
		} else if (new File(ioscheduler_mtd).exists()) {
			schedulerPath = ioscheduler_mtd;
		} else {
			return new String[] {};
			/*
			 * need to return an empty string and not a null because the wheel
			 * widget would just crash if a null value is returned
			 */
		}
		String[] schedulers = RootUtils.readOutputFromFile(schedulerPath)
				.split(" ");
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
		String currentScheduler = new String();
		String[] schedulers = RootUtils
				.readOutputFromFile(available_schedulers).split(" ");
		for (String string : schedulers) {
			if (string.contains("[")) {
				currentScheduler = string;
			}
		}
		return currentScheduler.substring(1, currentScheduler.length() - 1);
	}

	public static String getDefaultReadAhead() {
		String res = new String();
		for (int i = 0; i < 2; i++) {
			File device = new File(Constants.available_blockdevices + "mmcblk"
					+ i);
			if (device.exists()) {
				device = new File(Constants.available_blockdevices + "mmcblk"
						+ i + "/queue/read_ahead_kb");
				res = RootUtils.readOutputFromFile(device.getAbsolutePath());
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
				Log.d("no of cpus", noOfCpus + "");
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
						+ CpuUtils.scaling_governor.replace("cpu0", "cpu" + i)
						+ "\n");
				commands.add("echo " + minFrequency + " > "
						+ CpuUtils.scaling_min_freq.replace("cpu0", "cpu" + i)
						+ "\n");
				commands.add("echo " + maxFrequency.replace("cpu0", "cpu" + i)
						+ " > " + scaling_max_freq + "\n");

			}

		commands.add("exit" + "\n");
		RootUtils.executeRootCommand(commands);
		Toast.makeText(context, "Values Successfully Applied",
				Toast.LENGTH_SHORT).show();

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
			String readAhead) {

		ArrayList<String> mCommands = new ArrayList<String>();
		if (ioScheduler != null) {
			File[] devices = new File(available_blockdevices).listFiles();
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
				block = new File(available_blockdevices + "mmcblk" + i
						+ "/queue/read_ahead_kb");
				if (block.exists()) {
					mCommands.add("chmod 0644 " + block.getAbsolutePath()
							+ "\n");
					mCommands.add("echo " + readAhead + " > "
							+ block.getAbsolutePath() + "\n");

				}
			}
		}
		mCommands.add(" exit \n");
		RootUtils.executeRootCommand(mCommands);
	}

	public static String getKernelInfo() {
		String data = RootUtils.readOutputFromFile("/proc/version");
		return data;
	}

	public static String[] toMhz(String[] values) {
		String[] frequency = new String[values.length];
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				try {
					frequency[i] = (Integer.parseInt(values[i]) / 1000 + " Mhz");
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					return new String[] {};
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

}
