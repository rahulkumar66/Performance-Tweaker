package com.android.extras.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;

public class RootUtils implements Constants {
	
	
	public static boolean isRooted() {
		File file=new File("/system/bin/su");
		File f2=new File("/system/xbin/su");
		if(file.exists() || f2.exists())
			return true;
		else
			return false;		
		}
	
	
	public static boolean hasCpuFrequencyScaling() {
		String[] requiredFiles = {
				scaling_governor, scaling_max_freq, scaling_min_freq
		};
		for (String file : requiredFiles) {
			if (new File(file).exists()) {
				return true;
			}
			else
				return false;
			
		}
		return true;
		
	}
	
	
	public static String executeCommandwithResult(String comm) {
		StringBuffer buffer=new StringBuffer();
		String data=new String();
		Process process;
		BufferedReader stdinput;
		try {
			process=Runtime.getRuntime().exec(comm);
			stdinput=new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((data=stdinput.readLine())!=null){
				buffer.append(data);
			}	
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	
	public static void executeRootCommand(ArrayList<String> commands)  {
		InputStream is=null;
		DataOutputStream dos;
		try {
		Process mProcess=prepareRootShell();
		dos=new DataOutputStream
				(mProcess.getOutputStream());
			for(String cmd:commands) {
			dos.writeBytes(cmd);
			dos.flush();
		}
			if (mProcess.waitFor() == 0) {
				Log.d(tag,"Succesfull");
				is=mProcess.getInputStream();
				
			}
			else {
				Log.d(tag,"oh man");
				is=mProcess.getErrorStream();
			}
			dos.close();
			is.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		printOutputOnStdout(is);	
	}
	
	
	private static void printOutputOnStdout(InputStream is) {
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


	public static Process prepareRootShell() throws IOException {
		Process mProcess = Runtime.getRuntime().exec(getSUbinaryPath());
		return mProcess;
	}

	
	
	public static String getSUbinaryPath() {
		String path=executeCommandwithResult("which su");
		return path;
	}
	
	

}
