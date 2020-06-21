package com.performancetweaker.app.utils;

import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TimeInStateReader {

    private static final String TIME_IN_STATES = "/sys/devices/system/cpu/cpu%d/cpufreq/stats/time_in_state";
    private static final String TIME_IN_STATE_2 = "/sys/devices/system/cpu/cpufreq/stats/cpu%d/time_in_state";
    public Map<Integer, Long> newStates = new HashMap<>();
    private ArrayList<CpuState> states = new ArrayList<>();
    private ArrayList<CpuState> _states = new ArrayList<>();

    private long totalTime;

    private TimeInStateReader() {
    }

    public static TimeInStateReader TimeInStatesReader() {
        return new TimeInStateReader();
    }

    public ArrayList<CpuState> getCpuStateTime(boolean withDeepSleep, boolean filterZeroValues, int core) {
        states.clear();
        BufferedReader bufferedReader;
        Process process = null;
        String cpuTimeInStatesPath = TIME_IN_STATES.replace("%d", String.valueOf(core));
        String cpuTimeInStatesPath2 = TIME_IN_STATE_2.replace("%d", String.valueOf(core));
        File statsFile = new File(cpuTimeInStatesPath);
        File statsFile2 = new File(cpuTimeInStatesPath2);
        if (!statsFile.exists()) {
            if (statsFile.canRead()) {
                String line;
                try {
                    process = Runtime.getRuntime().exec("cat " + cpuTimeInStatesPath);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                try {
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] entries = line.split(" ");
                        long time = Long.parseLong(entries[1]);
                        states.add(new CpuState(Integer.parseInt(entries[0]), time));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (statsFile2.exists()) {
            String line;
            try {
                process = Runtime.getRuntime().exec("cat " + cpuTimeInStatesPath2);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    String[] entries = line.split(" ");
                    long time = Long.parseLong(entries[1]);
                    states.add(new CpuState(Integer.parseInt(entries[0]), time));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
         * calculate deep sleep time
         */
        if (withDeepSleep) {
            long deepSleepTime = (SystemClock.elapsedRealtime() - SystemClock.uptimeMillis()) / 10;
            if (deepSleepTime > 0) states.add(new CpuState(0, deepSleepTime));
        }
        if (states != null) {
            Collections.sort(states);
        }
        if (newStates.size() > 0) {
            int index = 0;
            for (CpuState iterable_element : states) {
                if (newStates.containsKey(iterable_element.frequency)) {
                    states.get(index).time -= newStates.get(iterable_element.frequency);
                }
                index++;
            }
        }
        return removeZeroValues();
    }

    private ArrayList<CpuState> removeZeroValues() {
        _states.clear();
        for (CpuState s : states) {
            if (s.time > 0) {
                _states.add(s);
            }
        }
        return _states;
    }

    public long getTotalTimeInState() {
        totalTime = 0;
        for (CpuState state : states) {
            totalTime += state.getTime();
        }
        return totalTime;
    }

    public void setNewStates(HashMap<Integer, Long> state) {
        clearNewStates();
        newStates = state;
    }

    public void clearNewStates() {
        newStates.clear();
    }
}
