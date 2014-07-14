package com.phantomLord.cpufrequtils.app.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;

public class RootUtils {

	public static boolean isRooted() {
		if (new File("/system/bin/su").exists()
				|| new File("/system/xbin/su").exists())
			return true;
		else
			return false;
	}

	public static String readOutputFromFile(String pathToFile) {

		StringBuffer buffer = new StringBuffer();
		String data = null;
		Process process;
		BufferedReader stdinput;
		File file = new File(pathToFile);
		if (!(file.exists())) {
			return "";
		}
		if (file.canRead()) {
			try {
				process = Runtime.getRuntime().exec("cat " + pathToFile);
				stdinput = new BufferedReader(new InputStreamReader(
						process.getInputStream()));
				while ((data = stdinput.readLine()) != null) {
					buffer.append(data);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return buffer.toString();

		}
		/*
		 * try reading the file as root
		 */
		else {
			InputStream inputStream;
			DataOutputStream dos;
			try {
				process = prepareRootShell();
				dos = new DataOutputStream(process.getOutputStream());
				dos.writeBytes("cat " + process);
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

	private static void printOutputOnStdout(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				Log.e(Constants.App_Tag, line);
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
