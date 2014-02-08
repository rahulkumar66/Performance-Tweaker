package com.android.extras.utils;

import java.util.ArrayList;

import android.content.Context;

public class CpuUtils implements Constants {

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
		for(int i=0;i<schedulers.length;i++) {
			if(schedulers[i].contains("]")) {
			String temp=schedulers[i].substring(1, schedulers[i].length()-1);
			schedulers[i]=temp;
			}
		}
		return schedulers;

	}
	
	public static final String getCurrentIOScheduler() {
		String currentScheduler=new String();
		String[] schedulers=RootUtils.executeCommandwithResult(
				"cat " + available_schedulers).split(" ");
		for (String string : schedulers) {
			if(string.contains("[")) {
				currentScheduler=string;
			}
		}
		//String str=currentScheduler.substring(1, currentScheduler.length()-1);
	//	Log.d(tag, currentScheduler.substring(1, currentScheduler.length()-1));
		return currentScheduler.substring(1, currentScheduler.length()-1);
	}
	

	
	public static final void setFrequencyAndGovernor(String max_frequency,
			String min_frequency, String governor,String ioscheduler, Context context) {
		String comm = "echo " + governor + " > " + CpuUtils.scaling_governor
				+ "\n";
		ArrayList<String> commands = new ArrayList<String>();
		// commands.add("chmod 0644 "+SysUtils.scaling_max_freq + " \n ");
		// commands.add("chmod 0644 " + SysUtils.scaling_min_freq + "\n");
		commands.add(comm);
		// Log.d(tag, comm);
		commands.add("echo " + min_frequency + " > "
				+ CpuUtils.scaling_min_freq + "\n");
		commands.add("echo " + max_frequency + " > " + scaling_max_freq + "\n");
		commands.add("exit" + "\n");
		RootUtils.executeRootCommand(commands);

	}


}
