package com.phantomLord.cpufrequtils.app.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;
import com.stericson.RootTools.RootTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CpuFrequencyFragment extends PreferenceFragment  {
    //ArrayWheelAdapter<String> frequencyAdapter;
    //ArrayWheelAdapter<String> governorAdapter;

    List<String> availableFrequencies = new ArrayList<String>();
    List<String> availableGovernors = new ArrayList<String>();

    String[] availablefreq;
    String[] availableScalingGovernors;
    String maxFrequency, minFrequency, currentGovernor;

    View mView;
    ActionBar actionBar;

    //AbstractWheel maxFreq, minimum, governor;

    Context themedContext, context;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.cpu_freq_preference);

    }



}
