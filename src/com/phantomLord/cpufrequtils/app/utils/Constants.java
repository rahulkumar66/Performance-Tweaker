package com.phantomLord.cpufrequtils.app.utils;

import java.util.HashMap;
import java.util.Map;

import com.phantomLord.cpufrequtils.app.R;

public class Constants {

	public final static String App_Tag = "Performace Tweaker";
	public final static String cpufreq_sys_dir = "/sys/devices/system/cpu/cpu0/cpufreq/";
	public final static String scaling_min_freq = cpufreq_sys_dir
			+ "scaling_min_freq";
	public final static String cpuinfo_min_freq = cpufreq_sys_dir
			+ "cpuinfo_min_freq";
	public final static String scaling_max_freq = cpufreq_sys_dir
			+ "scaling_max_freq";
	public final static String cpuinfo_max_freq = cpufreq_sys_dir
			+ "cpuinfo_max_freq";
	public final static String scaling_cur_freq = cpufreq_sys_dir
			+ "scaling_cur_freq";
	public final static String cpuinfo_cur_freq = cpufreq_sys_dir
			+ "cpuinfo_cur_freq";
	public final static String scaling_governor = cpufreq_sys_dir
			+ "scaling_governor";
	public final static String scaling_available_freq = cpufreq_sys_dir
			+ "scaling_available_frequencies";
	public final static String scaling_available_governors = cpufreq_sys_dir
			+ "scaling_available_governors";
	public final static String available_blockdevices = "/sys/block/";
	public final static String available_schedulers = "/sys/block/mmcblk0/queue/scheduler";
	public final static String available_schedulers_path = "/sys/block/mmcblk1/queue/scheduler";
	public static final String time_in_states = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
	public final static String ioscheduler_mtd = "/sys/block/mtdblock0/queue/scheduler";
	public static final String SD_CACHE = "/sys/devices/virtual/bdi/179:0/read_ahead_kb";

	public final static String PREF_ZERO_VALS = "non_zero_vals_only";

	public static final String[] readAheadKb = { "64", "128", "256", "384",
			"512", "640", "768", "896", "1024", "1152", "1280", "1408", "1536",
			"1664", "1792", "1920", "2048", "2176", "2304", "2432", "2560",
			"2688", "2816", "2944", "3072", "3200", "3328", "3456", "3584",
			"3712", "3840", "3968", "4096" };

	public final static String[] mFragmentsArray = new String[] {
			"Cpu Frequency", "Time In State", "I/0 Control", "Wakelocks" };
	public final static String[] wakelockTypes = new String[] {
			"Kernel Wakelocks", "Cpu Wakelocks", "Wakeup Triggers" };

	public static final int icons[] = new int[] { R.drawable.meter,
			R.drawable.bar_chart, R.drawable.backup, R.drawable.battery_med };
	public static final int actionBarIcons[] = new int[] { R.drawable.sysctl,
			R.drawable.main_cpu, R.drawable.changelog };
	public static final int theme[] = new int[] { R.style.Theme_Sherlock,
			R.style.Theme_Sherlock_Light,
			R.style.Theme_Sherlock_Light_DarkActionBar };

	public static final Map<String, Integer> THEMES_MAP = new HashMap<String, Integer>() {

		private static final long serialVersionUID = 1552737519285513057L;
		{
			put("Dark", R.style.Theme_Sherlock);
			put("Light", R.style.Theme_Sherlock_Light);
			put("Light_DarkActionBar",
					R.style.Theme_Sherlock_Light_DarkActionBar);
		}
	};

}
