package com.phantomLord.cpufrequtils.app.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;

public class CpuFrequencyFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    String[] availablefreq;
    String[] availableGovernors;
    String maxFrequency, minFrequency, currentGovernor;

    ListPreference CpuMaxFreqPreference;
    ListPreference CpuMinFreqPreference;
    ListPreference GovernorPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.cpu_freq_preference);

        CpuMaxFreqPreference = (ListPreference) findPreference("cpu_max_freq_pref");
        CpuMinFreqPreference = (ListPreference) findPreference("cpu_min_freq_pref");
        GovernorPreference = (ListPreference) findPreference("governor_pref");
        populatePreferences();

        CpuMaxFreqPreference.setOnPreferenceChangeListener(this);
        CpuMinFreqPreference.setOnPreferenceChangeListener(this);
        GovernorPreference.setOnPreferenceChangeListener(this);


    }

    public void populatePreferences() {
        availablefreq = SysUtils.getAvailableFrequencies();
        availableGovernors = SysUtils.getAvailableGovernors();
        currentGovernor = SysUtils.getCurrentScalingGovernor();
//        maxFrequency = SysUtils.getCurrentMaxFrequeny();
  //      minFrequency = SysUtils.getCurrentMinFrequency();

        if (availablefreq != null) {
            CpuMaxFreqPreference.setEntries(availablefreq);
            CpuMaxFreqPreference.setEntryValues(availablefreq);
            CpuMinFreqPreference.setEntries(availablefreq);
            CpuMinFreqPreference.setEntryValues(availablefreq);
        }
        if (availableGovernors != null) {
            GovernorPreference.setEntries(availableGovernors);
            GovernorPreference.setEntryValues(availableGovernors);
        }
        if (maxFrequency != null && minFrequency != null && currentGovernor!=null) {
            CpuMaxFreqPreference.setDefaultValue(SysUtils.getCurrentMaxFrequeny());
            CpuMinFreqPreference.setDefaultValue(SysUtils.getCurrentMinFrequency());
            GovernorPreference.setDefaultValue(SysUtils.getCurrentScalingGovernor());
        }
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if(preference.getKey().equals("cpu_min_freq_pref")) {

        }
        if(preference.equals("cpu_max_freq_pref")) {

        }
        if(preference.equals("governor_pref")) {

        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.set_on_boot_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myswitch:
                Toast.makeText(getActivity().getBaseContext(), "Applying on boot",
                        Toast.LENGTH_SHORT)
                        .show();
        }
        return super.onOptionsItemSelected(item);

    }
}
