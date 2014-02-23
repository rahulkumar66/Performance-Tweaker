package com.rattlehead.cpufrequtils.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;

public class CpuUtils implements Constants {

	private static long totaltime = 0;
	private static ArrayList<CpuState> states = new ArrayList<CpuState>();

	public static String[] getAvailableFrequencies() {
		String frequency[] = RootUtils.executeCommandwithResult(
				"cat " + scaling_available_freq).split(" ");
		return frequency;
	}

	public static String getCurrentMaxFrequeny() {
		String result = RootUtils.executeCommandwithResult("cat "
				+ scaling_cur_freq);
		return result;
	}

	public static String getCurrentMinFrequency() {
		String result = RootUtils.executeCommandwithResult("cat "
				+ scaling_min_freq);
		return result;
	}

	public static String[] getAvailableGovernors() {
		String governors = RootUtils.executeCommandwithResult("cat "
				+ scaling_available_governors);
		// Log.d(tag, governors);
		String[] gov = governors.split(" ");
		return gov;
	}

	public static String getCurrentScalingGovernor() {
		String result = RootUtils.executeCommandwithResult("cat "
				+ scaling_governor);
		return result;
	}

	public static final String[] getAvailableIOScheduler() {
		String[] schedulers = RootUtils.executeCommandwithResult(
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
		String[] schedulers = RootUtils.executeCommandwithResult(
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
		String comm = "echo " + governor + " > " + CpuUtils.scaling_governor
				+ "\n";
		ArrayList<String> commands = new ArrayList<String>();
		// commands.add("chmod 0644 "+SysUtils.scaling_max_freq + " \n ");
		// commands.add("chmod 0644 " + SysUtils.scaling_min_freq + "\n");
		commands.add(comm);
		commands.add("echo " + min_frequency + " > "
				+ CpuUtils.scaling_min_freq + "\n");
		commands.add("echo " + max_frequency + " > " + scaling_max_freq + "\n");
		commands.add("exit" + "\n");
		RootUtils.executeRootCommand(commands);

	}

	@SuppressLint("UseSparseArrays")
	public static ArrayList<CpuState> getCpuStateTime() {
		states.clear();
		String line;
		BufferedReader bufferedReader = RootUtils.getBufferForCommand("cat "
				+ Constants.time_in_states);
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String entries[] = line.split(" ");
				long time = Long.parseLong(entries[1]) / 100;
				states.add(new CpuState(Integer.parseInt(entries[0]), time));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long deepSleepTime = (SystemClock.elapsedRealtime() - SystemClock
				.uptimeMillis());
		long seconds = TimeUnit.MILLISECONDS.toSeconds(deepSleepTime);
		if (deepSleepTime > 0)
			states.add(new CpuState(0, seconds));
		return states;
	}

	public static long getTotalTimeInState() {
		totaltime = 0;
		for (CpuState state : states) {
			totaltime += state.getTime();
		}

		return totaltime;
	}

	public static void clearCpuStates() {
		states.clear();
	}

}
