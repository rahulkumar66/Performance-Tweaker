package com.rattlehead666.performancetweaker.app.utils;

import com.rattlehead666.performancetweaker.app.BuildConfig;
import com.rattlehead666.performancetweaker.app.R;

public interface Constants {

  String App_Tag = "Performance Tweaker";
  boolean debug = BuildConfig.DEBUG;
  /*
  Cpu
  */ String cpufreq_sys_dir = "/sys/devices/system/cpu/cpu0/cpufreq/";
  String scaling_min_freq = cpufreq_sys_dir + "scaling_min_freq";
  String cpuinfo_min_freq = cpufreq_sys_dir + "cpuinfo_min_freq";
  String scaling_max_freq = cpufreq_sys_dir + "scaling_max_freq";

  String cpuinfo_max_freq = cpufreq_sys_dir + "cpuinfo_max_freq";
  String scaling_cur_freq = cpufreq_sys_dir + "scaling_cur_freq";
  String cpuinfo_cur_freq = cpufreq_sys_dir + "cpuinfo_cur_freq";
  String scaling_governor = cpufreq_sys_dir + "scaling_governor";
  String scaling_available_freq = cpufreq_sys_dir + "scaling_available_frequencies";
  String scaling_available_governors = cpufreq_sys_dir + "scaling_available_governors";
  String governor_prop_dir = "/sys/devices/system/cpu/cpufreq/";
  /*
  I/O
   */ String available_blockdevices = "/sys/block/";
  String available_schedulers = "/sys/block/mmcblk0/queue/scheduler";
  String available_schedulers_path = "/sys/block/mmcblk1/queue/scheduler";
  String time_in_states = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
  String ioscheduler_mtd = "/sys/block/mtdblock0/queue/scheduler";
  String SD_CACHE = "/sys/devices/virtual/bdi/179:0/read_ahead_kb";
  String[] readAheadKb = {
      "64", "128", "256", "384", "512", "640", "768", "896", "1024", "1152", "1280", "1408", "1536",
      "1664", "1792", "1920", "2048", "2176", "2304", "2432", "2560", "2688", "2816", "2944",
      "3072", "3200", "3328", "3456", "3584", "3712", "3840", "3968", "4096"
  };
  /*
  GPU
   */ String[] GPU_PATH = new String[] {
      "/sys/class/kgsl", "/sys/devices/platform/galcore/gpu/gpu0/gpufreq"
  };
  String[] gpu_governor_path = new String[] {
      "/kgsl-3d0/pwrscale/trustzone/governor", "/kgsl-3d0/devfreq/governor", "/scaling_governor"
  };
  String[] gpu_govs_avail_path = new String[] {
      "/kgsl-3d0/devfreq/available_governors", "/scaling_available_governors"
  };
  String[] gpu_freqs_avail = new String[] {
      "/kgsl-3d0/gpu_available_frequencies", "/kgsl-3d0/devfreq/available_frequencies",
      "/scaling_available_frequencies"
  };
  String[] gpu_freqs_max = new String[] {
      "/kgsl-3d0/max_gpuclk", "/kgsl-3d0/devfreq/max_freq", "/scaling_max_freq"
  };
  String[] gpu_freqs_min = new String[] {
      "/kgsl-3d0/min_gpuclk", "/kgsl-3d0/devfreq/min_freq", "/scaling_min_freq"
  };
  String[] mFragmentsArray = new String[] {
      "Cpu Frequency", "Time In State", "I/0 Control", "Wakelocks", "Settings"
  };
  int icons[] = new int[] {
      R.drawable.ic_action_meter, R.drawable.ic_action_bar_chart, R.drawable.ic_action_backup,
      R.drawable.ic_action_battery_med, R.drawable.ic_action_prefs_widget
  };
  String[] wakelockTypes = new String[] {
      "Kernel Wakelocks", "Cpu Wakelocks", "Wakeup Triggers"
  };
  /*
   * Preferences
   */ String PREF_ZERO_VALS = "non_zero_vals_only";
  String PREF_MAX_FREQ = "max_freq";
  String PREF_MIN_FREQ = "min_freq";
  String PREF_GOV = "governor";
  String PREF_CPU_APPLY_ON_BOOT = "cpu_apply_on_boot";
  String PREF_IO_APPLY_ON_BOOT = "io_apply_boot";
  String PREF_IO_SCHEDULER = "io_scheduler";
  String PREF_READ_AHEAD = "read_ahead";
  String PREF_TIS_RESET_STATS = "tis_reset_stats";
  String CPU_0 = "Core 0";
  String CPU_1 = "Core 1";
  String CPU_2 = "Core 2";
  String CPU_3 = "Core 3";
  String CPU_ALL = "All Cores";
}
