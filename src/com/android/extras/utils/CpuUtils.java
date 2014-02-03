package com.android.extras.utils;


import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class CpuUtils implements Constants {

		public static String[] getAvailableFrequencies() {
			String AllFrequncies=RootUtils.executeCommandwithResult("cat "+scaling_available_freq);
			Log.d("Frequencies",AllFrequncies);
			String frequency[]=AllFrequncies.split(" ");
			return frequency;
		}	
		
	
		public static String getCurrentMaxFrequeny(){
			String result=RootUtils.executeCommandwithResult("cat "+scaling_cur_freq);
			return result;	
		}
	
		
		public static String getCurrentMinFrequency() {
			String result=RootUtils.executeCommandwithResult("cat "+scaling_min_freq);
			return result;
		}
		
		
		public static String[] getAvailableGovernors() {
			String governors=RootUtils.executeCommandwithResult
					("cat "+scaling_available_governors);
			//Log.d(tag, governors);
			String[] gov=governors.split(" ");
			return gov;
			}
		
		
		public static String getCurrentScalingGovernor() {
			String result=RootUtils.executeCommandwithResult("cat "+scaling_governor);
			return result;
		}
		
	
		public static final void setFrequencyAndGovernor(String max_frequency,
			String min_frequency,String governor,Context context) {
			String comm="echo "+governor + " > "+CpuUtils.scaling_governor+"\n"; 
			ArrayList<String> commands=new ArrayList<String>();
			//commands.add("chmod 0644 "+SysUtils.scaling_max_freq + " \n ");
			//commands.add("chmod 0644 " + SysUtils.scaling_min_freq + "\n");
			commands.add(comm);
			Log.d(tag, comm);
			commands.add("echo " + min_frequency + " > " + CpuUtils.scaling_min_freq+ "\n");
			commands.add("echo "+max_frequency+" > "+scaling_max_freq + "\n");
			commands.add("exit" + "\n");
			RootUtils.executeRootCommand(commands);
			
			
		}
		
		
		public static int getCurentGovernorIndex() {
			return -1;
		}
}

