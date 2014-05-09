package com.phantomLord.cpufrequtils.app.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

public class CpuUtils implements Constants {

	public static String[] getAvailableFrequencies() {
		String[] frequencies = null;
		if (new File(scaling_available_freq).exists()) {
			frequencies = RootUtils.executeCommand(
					"cat " + scaling_available_freq).split(" ");
			return frequencies;
		} else if (new File(time_in_states).exists()) {
			ArrayList<CpuState> states = new ArrayList<CpuState>();
			int i = 0;
			states = new TimeInStateReader().getCpuStateTime(false);
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

		return RootUtils.executeCommand("cat " + scaling_max_freq);
	}

	public static String getCurrentMinFrequency() {
		return RootUtils.executeCommand("cat " + scaling_min_freq);
	}

	public static String[] getAvailableGovernors() {
		return RootUtils.executeCommand("cat " + scaling_available_governors)
				.split(" ");
	}

	public static String getCurrentScalingGovernor() {
		return RootUtils.executeCommand("cat " + scaling_governor);
	}

	public static final String[] getAvailableIOScheduler() {
		String schedulerPath = new String();
		if (new File(available_schedulers).exists()) {
			schedulerPath = available_schedulers;

		} else if (new File(available_schedulers_path).exists()) {
			schedulerPath = available_schedulers_path;
			/*
			 * Some devices dont have mmcblk0 so we use mtdblock0 to read the available 
			 * schedulers
			 */
		} else if (new File(ioscheduler_mtd).exists()) {
			schedulerPath=ioscheduler_mtd;
		} else {
			return new String[] {};
			/*
			 * need to return an empty string and not a null because the wheel
			 * widget would just crash if a null value is returned
			 */
		}
		String[] schedulers = RootUtils.executeCommand("cat " + schedulerPath)
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
		String[] schedulers = RootUtils.executeCommand(
				"cat " + available_schedulers).split(" ");
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
				res = RootUtils.executeCommand("cat "
						+ device.getAbsolutePath());
			}
		}
		return res;

	}

	public static final void setFrequencyAndGovernor(String maxFrequency,
			String minFrequency, String governor, Context context) {
		ArrayList<String> commands = new ArrayList<String>();
		if (maxFrequency != null && minFrequency != null && governor != null) {
			commands.add("echo " + governor + " > " + CpuUtils.scaling_governor
					+ "\n");
			commands.add("echo " + minFrequency + " > "
					+ CpuUtils.scaling_min_freq + "\n");
			commands.add("echo " + maxFrequency + " > " + scaling_max_freq
					+ "\n");
			commands.add("exit" + "\n");
			RootUtils.executeRootCommand(commands);
			Toast.makeText(context, "Values Successfully Applied",
					Toast.LENGTH_SHORT).show();

		}

	}

	public static void setDiskSchedulerandReadAhead(String ioScheduler,
			String readAhead) {
		ArrayList<String> mCommands = new ArrayList<String>();
		File devices = new File(available_blockdevices);

		String[] directories = devices.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		for (int i = 0; i < directories.length; i++) {
			if (!(directories[i].contains("ram")
					|| directories[i].contains("loop") || directories[i]
						.contains("dm"))) {
				File blockDevice = new File(available_blockdevices
						+ directories[i] + "/queue/scheduler");
				if (blockDevice.exists())
					mCommands.add("echo " + ioScheduler + " > "
							+ blockDevice.getAbsolutePath() + " \n ");
			}
		}
		/*
		 * prepare commands for changing the read ahead cache
		 */
		File block;
		for (int i = 0; i < 2; i++) {
			block = new File(available_blockdevices + "mmcblk" + i
					+ "/queue/read_ahead_kb");
			if (block.exists()) {
				mCommands.add("echo " + readAhead + " > "
						+ block.getAbsolutePath() + "\n");

			}
		}
		mCommands.add(" exit \n");
		RootUtils.executeRootCommand(mCommands);

	}

	public static ArrayList<String> toArrayList(String[] contents) {
		ArrayList<String> data = new ArrayList<String>();
		if (contents != null) {
			for (String str : contents) {
				data.add(str);
			}
		}
		return data;
	}

	public static String getKernelInfo() {
		String data = RootUtils.executeCommand("cat /proc/version");
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
					 * thrown while parsing the integer values if we return a
					 * null value the spinner wheel widget would just crash and
					 * we dont want that do we??
					 */
				}
			}

		}
		return frequency;

	}

}
