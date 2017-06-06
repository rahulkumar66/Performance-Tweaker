package com.performancetweaker.app.utils;

import com.asksven.android.common.privateapiproxies.StatElement;

import java.io.Serializable;
import java.util.ArrayList;

public class WakelockReference implements Serializable {

    private static final long serialVersionUID = 1L;
    protected ArrayList<StatElement> mRefWakelocks = null;
    protected ArrayList<StatElement> mRefKernelWakelock = null;
    protected ArrayList<StatElement> mRefAlarms = null;
    protected long timeSince = 100;
    private String STATS_TYPE;
    private String STATS_SINCE_UNPLUGGED = "since_unplugged";
    private String STATS_SINCE_BOOT = "since_boot";
    private String STAT_CUSTOM_REFERENCE = "custom_red";

    private WakelockReference() {

    }

    public WakelockReference(String statsType) {

    }
}