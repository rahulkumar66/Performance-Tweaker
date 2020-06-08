package com.performancetweaker.app.utils;

import com.performancetweaker.app.BuildConfig;
import com.performancetweaker.app.R;

public interface Constants {

    String App_Tag = "Performance Tweaker";
    String packageName = "com.performancetweaker.app";
    boolean debug = BuildConfig.DEBUG;

    // CPU
    String cpufreq_sys_dir = "/sys/devices/system/cpu/cpu0/cpufreq/";
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

    // I/O
    String available_blockdevices = "/sys/block/";
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

    String[] mFragmentsArray = new String[]{
            "Cpu Frequency", "Time In State", "I/0 Control", "Wakelocks", "Settings"
    };

    // icons
    int icons[] = new int[]{
            R.drawable.ic_action_meter, R.drawable.ic_action_bar_chart, R.drawable.ic_action_backup,
            R.drawable.ic_action_battery_med, R.drawable.ic_action_prefs_widget
    };

    // Wakelocks
    String[] wakelockTypes = new String[]{
            "Kernel Wakelocks", "Cpu Wakelocks", "Wakeup Triggers"
    };

    // Preferences
    String PREF_CPU_MAX_FREQ = "cpu_max_freq_pref";
    String PREF_CPU_MIN_FREQ = "cpu_min_freq_pref";
    String PREF_CPU_GOV = "governor_pref";
    String PREF_GPU_MAX = "gpu_max_freq_pref";
    String PREF_GPU_MIN = "gpu_min_freq_pref";
    String PREF_GPU_GOV = "gpu_governor_pref";
    String PREF_HOTPLUG = "cpu_hotplug";
    String PREF_IO_SCHEDULER = "disk_scheduler";
    String PREF_READ_AHEAD = "read_ahead_cache";
    String PREF_TIS_RESET_STATS = "tis_reset_stats";
    String PREF_ZERO_VALS = "non_zero_vals_only";

    // Build prop
    String BUILD_PROP = "/system/build.prop";

    // Virtual Memory
    String VM_PATH = "/proc/sys/vm";
    String[] SUPPORTED_VM = {
            "dirty_ratio", "dirty_background_ratio", "dirty_expire_centisecs",
            "dirty_writeback_centisecs", "min_free_kbytes", "overcommit_ratio", "swappiness",
            "vfs_cache_pressure", "laptop_mode", "extra_free_kbytes"
    };

    // CPU Hotplug
    String HOTPLUG_MPDEC = "mpdecision";

    String APK_NAME = "performancetweaker.apk";

    String FAN_INTERSTITIAL_ID = (BuildConfig.BUILD_TYPE == "debug" ? "IMG_16_9_APP_INSTALL#" : "") + "2399395553684712_2400349406922660";
    String FAN_BANNER_ID = (BuildConfig.BUILD_TYPE == "debug" ? "IMG_16_9_APP_INSTALL#" : "") + "2399395553684712_2400350063589261";
}
