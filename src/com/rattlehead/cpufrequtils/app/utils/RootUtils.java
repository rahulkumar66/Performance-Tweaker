package com.rattlehead.cpufrequtils.app.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;

public class RootUtils extends Thread implements Constants {

	public static boolean isRooted() {
		if (new File("/system/bin/su").exists()
				|| new File("/system/xbin/su").exists())
			return true;
		else
			return false;
	}

	public static String executeCommand(String comm) {

		StringBuffer buffer = new StringBuffer();
		String data = null;
		Process process;
		BufferedReader stdinput;
		try {
			process = Runtime.getRuntime().exec(comm);
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

	public static BufferedReader getBufferForCommand(String command) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return new BufferedReader(new InputStreamReader(
				process.getInputStream()));
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
				Log.d(tag, "rattle you goddamn head");
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
				Log.e(tag, line);
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
		String path = RootUtils.executeCommand("which su");
		return path;
	}

}
