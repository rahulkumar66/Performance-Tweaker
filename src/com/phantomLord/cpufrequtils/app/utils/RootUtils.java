package com.phantomLord.cpufrequtils.app.utils;

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
		if (new File("/system/bin/su").exists()
				|| new File("/system/xbin/su").exists())
			return true;
		else
			return false;
	}

	public static boolean hasSysfs() {
		String[] requiredFiles = { scaling_governor, scaling_max_freq,
				scaling_min_freq };
		for (String requiredFile : requiredFiles) {
			if (!(new File(cpufreq_sys_dir + requiredFile)).exists()) {
				Log.d(Constants.App_Tag, new File(cpufreq_sys_dir
						+ requiredFile).getAbsolutePath().toString());
				return false;
			}
		}
		return true;
	}

	public static String executeCommand(String command) {

		StringBuffer buffer = new StringBuffer();
		String data = null;
		Process process;
		BufferedReader stdinput;
		try {
			process = Runtime.getRuntime().exec(command);
			stdinput = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			while ((data = stdinput.readLine()) != null) {
				buffer.append(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();

	}

	public static void executeRootCommand(ArrayList<String> commands) {
		InputStream is = null;
		DataOutputStream dos;
		try {
			Process mProcess = prepareRootShell();
			dos = new DataOutputStream(mProcess.getOutputStream());
			for (String cmd : commands) {
				dos.writeBytes(cmd);
				dos.flush();
			}
			if (mProcess.waitFor() == 0) {
				is = mProcess.getInputStream();
			} else {
				is = mProcess.getErrorStream();
			}
			dos.close();
			if (is != null)
				printOutputOnStdout(is);
			is.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String executeRootCommandWithResult(String command) {
		InputStream inputStream;
		DataOutputStream dos;
		String data = new String();
		try {
			Process process = prepareRootShell();
			dos = new DataOutputStream(process.getOutputStream());
			dos.writeBytes(command);
			dos.flush();
			dos.writeBytes("\n exit ");
			dos.flush();
			dos.close();
			if (process.waitFor() == 0) {
				inputStream = process.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;

				while ((line = reader.readLine()) != null) {
					data = line;
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return data;

	}

	private static void printOutputOnStdout(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				Log.e(App_Tag, line);
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
		String path = "/system/bin/su";
		if (new File(path).exists()) {
			return path;
		}
		path = "/system/xbin/su";
		if (new File(path).exists()) {
			return path;
		}
		return null;
	}

}
