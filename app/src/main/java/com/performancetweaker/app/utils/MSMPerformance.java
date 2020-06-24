package com.performancetweaker.app.utils;

import android.content.Context;

public class MSMPerformance {

    private static MSMPerformance sInstance;

    public static MSMPerformance getInstance() {
        if (sInstance == null) {
            sInstance = new MSMPerformance();
        }
        return sInstance;
    }

    public static final String PARENT = "/sys/module/msm_performance";
    public static final String MAX_CPUS = PARENT + "/parameters/max_cpus";
    private static final String CPU_MAX_FREQ = PARENT + "/parameters/cpu_max_freq";
    private static final String MAX_CPU_FREQ = PARENT + "/parameters/max_cpu_freq";
    private static final String CPU_MIN_FREQ = PARENT + "/parameters/cpu_min_freq";

    private Boolean MAX_CPUS_SUPPORTED;
    private String CPU_MAX_FREQ_FILE;
    private Boolean CPU_MIN_FREQ_SUPPORTED;

    private MSMPerformance() {
        MAX_CPUS_SUPPORTED = Utils.fileExists(MAX_CPUS);

        if (Utils.fileExists(CPU_MAX_FREQ)) {
            CPU_MAX_FREQ_FILE = CPU_MAX_FREQ;
        } else if (Utils.fileExists(MAX_CPU_FREQ)) {
            CPU_MAX_FREQ_FILE = MAX_CPU_FREQ;
        }

        CPU_MIN_FREQ_SUPPORTED = Utils.fileExists(CPU_MIN_FREQ);
    }

    void setCpuMinFreq(int freq, int cpu, Context context) {
//        run(Control.write(cpu + ":" + freq, CPU_MIN_FREQ), CPU_MIN_FREQ + cpu, context);
    }

    boolean hasCpuMinFreq() {
        return CPU_MIN_FREQ_SUPPORTED;
    }

    void setCpuMaxFreq(int freq, int cpu, Context context) {
//        run(Control.write(cpu + ":" + freq, CPU_MAX_FREQ_FILE), CPU_MAX_FREQ_FILE + cpu, context);
    }

    boolean hasCpuMaxFreq() {
        return CPU_MAX_FREQ_FILE != null;
    }

    boolean hasMaxCpus() {
        return MAX_CPUS_SUPPORTED;
    }

    public boolean supported() {
        return hasMaxCpus() || hasCpuMaxFreq() || hasCpuMinFreq();
    }

    private void run(String command, String id, Context context) {
//        Control.runSetting(command, ApplyOnBootFragment.CPU, id, context);
    }
}