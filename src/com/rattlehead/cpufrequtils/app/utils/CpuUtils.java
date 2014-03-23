package com.rattlehead.cpufrequtils.app.utils;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;

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
				frequencies[i] = object.getFrequency() + "";
				i++;
			}
			return frequencies;
		} else {
			frequencies = new String[2];
			frequencies[0] = getCurrentMaxFrequeny();
			frequencies[1] = getCurrentMinFrequency();
			return frequencies;
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
		String[] schedulers = RootUtils.executeCommand(
				"cat " + available_schedulers).split(" ");
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

	public static final void setFrequencyAndGovernor(String max_frequency,
			String min_frequency, String governor, String ioscheduler,
			Context context) {
		ArrayList<String> commands = new ArrayList<String>();
		if (max_frequency != null && min_frequency != null && governor != null) {
			commands.add("echo " + governor + " > " + CpuUtils.scaling_governor
					+ "\n");
			commands.add("echo " + min_frequency + " > "
					+ CpuUtils.scaling_min_freq + "\n");
			commands.add("echo " + max_frequency + " > " + scaling_max_freq
					+ "\n");
			commands.add("exit" + "\n");
			RootUtils.executeRootCommand(commands);
		}

	}

	public static ArrayList<String> toArrayList(String[] contents) {
		ArrayList<String> data = new ArrayList<String>();
		for (String str : contents) {
			data.add(str);
		}
		return data;
	}

	public static String[] toMhz(String[] values) {
		int i = 0;
		String[] frequency = new String[values.length];
		if (values != null) {
			for (i = 0; i < values.length; i++) {
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
