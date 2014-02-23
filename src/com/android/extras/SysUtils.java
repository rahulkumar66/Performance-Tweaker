package com.android.extras;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class SysUtils {
	//hello
	private final static String cpufreq_sys_dir = "/sys/devices/system/cpu/cpu0/cpufreq/";
	private final static String scaling_min_freq = cpufreq_sys_dir + "scaling_min_freq";
	private final static String cpuinfo_min_freq = cpufreq_sys_dir + "cpuinfo_min_freq";
	private final static String scaling_max_freq = cpufreq_sys_dir + "scaling_max_freq";
	private final static String cpuinfo_max_freq = cpufreq_sys_dir + "cpuinfo_max_freq";
	private final static String scaling_cur_freq = cpufreq_sys_dir + "scaling_cur_freq";
	private final static String cpuinfo_cur_freq = cpufreq_sys_dir + "cpuinfo_cur_freq";
	private final static String scaling_governor = cpufreq_sys_dir + "scaling_governor";
	private final static String scaling_available_freq = cpufreq_sys_dir + "scaling_available_frequencies";
	private final static String scaling_available_governors = cpufreq_sys_dir + "scaling_available_governors";
	private final static String tag="Cpu Freq Utilities";

	
		public static String[] getAvailableFrequencies() {
			String AllFrequncies=executeCommandwithResult("cat "+scaling_available_freq);
			Log.d("Frequencies",AllFrequncies);
			String frequency[]=AllFrequncies.split(" ");
			return frequency;
		}
	
	
		public static boolean isRooted() {
			File file=new File("/system/bin/su");
			File f2=new File("/system/xbin/su");
			if(file.exists() || f2.exists())
				return true;
			else
				return false;		
			}	
		
	
		public static String getCurrentMaxFrequeny(){
			String result=executeCommandwithResult("cat "+scaling_cur_freq);
			return result;	
		}
	
		
		public static String getCurrentMinFrequency() {
			String result=executeCommandwithResult("cat "+scaling_min_freq);
			return result;
		}
		
	
		public static String executeCommandwithResult(String comm) {
			StringBuffer res=new StringBuffer();
			String data=new String();
			Process process;
			BufferedReader stdinput;
			try {
				process=Runtime.getRuntime().exec(comm);
				stdinput=new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((data=stdinput.readLine())!=null){
					res.append(data);
				}	
			}
			catch(IOException e){
				e.printStackTrace();
			}
			return res.toString();
		}
	
	
		public static final void setFrequencyAndGovernor(String max_frequency,String min_frequency) {
			String command;
			InputStream is=null;
			command="echo "+max_frequency+" > "+scaling_max_freq;
			Log.d(tag, command);
			List<String> commands=new ArrayList<String>();
			//commands.add("chmod 0644 "+SysUtils.scaling_max_freq + " \n ");
			//commands.add("chmod 0644 " + SysUtils.scaling_min_freq + "\n");
			commands.add("echo " + min_frequency + " > " + SysUtils.scaling_min_freq+ "\n");
			commands.add("echo "+max_frequency+" > "+scaling_max_freq + "\n");
			commands.add("exit" + "\n");
			
			try {
				Process p = Runtime.getRuntime().exec(getSUbinaryPath());
				DataOutputStream dos = new DataOutputStream(p.getOutputStream());
				for(String cmd:commands) {
					dos.writeBytes(cmd);
					dos.flush();
				}
				dos.close();
				int res = p.waitFor();
				if (res == 0) {
					Log.d(tag,"Succesfull");
					is=p.getInputStream();
				}
				else {
					Log.d(tag,"oh man");
					is=p.getErrorStream();
				}
				} catch (IOException e) {
				e.printStackTrace();
				}
			 	catch (InterruptedException e) {
					e.printStackTrace();
				}
				BufferedReader br=new BufferedReader(new InputStreamReader(is));
				String line = null;
				try {
					while ((line = br.readLine()) != null) {
						Log.d(tag, line);
						}
				} catch (IOException e) {
					e.printStackTrace();
				}
					
		}
	
	
		public static String getSUbinaryPath() {
			String s = "/system/bin/su";
			File f = new File(s);
			if (f.exists()) {
				return s;
			}
			s = "/system/xbin/su";
			f = new File(s);
			if (f.exists()) {
				return s;
			}
			return null;
		}
		
		
		public static int getCurrentMaxFrequencyIndex(String[] frequencies) {
			String currentMaxFrequency=getCurrentMaxFrequeny();
			for(int i=0;i<frequencies.length;i++) {
				if(currentMaxFrequency.equals(frequencies[i])) 
					return i;
			}
			return -1;
		}
		
		
		public static int getCurrentMinFrequencyIndex(String[] frequencies) {
			String currentMinFrequency=getCurrentMinFrequency();
			for(int i=0;i<frequencies.length;i++) {
				if(currentMinFrequency.equals(frequencies[i])) 
					return i;
			}
			return -1;
		}
}

