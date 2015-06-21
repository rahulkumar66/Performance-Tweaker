package com.phantomLord.cpufrequtils.app.ui;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.CpuFrequencyUtils;
import com.phantomLord.cpufrequtils.app.utils.GovernorProperties;

public class CpuFrequencyFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    String[] availablefreq;
    String[] availableGovernors;
    String maxFrequency, minFrequency, currentGovernor;

    ListPreference CpuMaxFreqPreference;
    ListPreference CpuMinFreqPreference;
    ListPreference GovernorPreference;
    Preference preferenceScreen;
    PreferenceCategory preferenceCategory;
    EditTextPreference editTextPreferences[];
    GovernorProperties[] governorProperties;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.cpu_freq_preference);

        CpuMaxFreqPreference = (ListPreference) findPreference("cpu_max_freq_pref");
        CpuMinFreqPreference = (ListPreference) findPreference("cpu_min_freq_pref");
        GovernorPreference = (ListPreference) findPreference("governor_pref");
        preferenceCategory = (PreferenceCategory) findPreference("governor_tune_pref");
        populatePreferences();

        CpuMaxFreqPreference.setOnPreferenceChangeListener(this);
        CpuMinFreqPreference.setOnPreferenceChangeListener(this);
        GovernorPreference.setOnPreferenceChangeListener(this);

    }

    public void populatePreferences() {
        availablefreq = CpuFrequencyUtils.getAvailableFrequencies();
        availableGovernors = CpuFrequencyUtils.getAvailableGovernors();
        currentGovernor = CpuFrequencyUtils.getCurrentScalingGovernor();
        maxFrequency = CpuFrequencyUtils.getCurrentMaxFrequeny();
        minFrequency = CpuFrequencyUtils.getCurrentMinFrequency();

        if (availablefreq != null) {
            CpuMaxFreqPreference.setEntries(CpuFrequencyUtils.toMhz(availablefreq));
            CpuMaxFreqPreference.setEntryValues(availablefreq);
            CpuMinFreqPreference.setEntries(CpuFrequencyUtils.toMhz(availablefreq));
            CpuMinFreqPreference.setEntryValues(availablefreq);
        }
        if (availableGovernors != null) {
            GovernorPreference.setEntries(availableGovernors);
            GovernorPreference.setEntryValues(availableGovernors);
        }
        if (maxFrequency != null && minFrequency != null && currentGovernor != null) {
            CpuMaxFreqPreference.setDefaultValue(maxFrequency);
            CpuMinFreqPreference.setDefaultValue(minFrequency);
            GovernorPreference.setDefaultValue(currentGovernor);
        }

        governorProperties = CpuFrequencyUtils.getGovernorProperties();
        if (governorProperties != null && governorProperties.length != 0) {
            editTextPreferences = new EditTextPreference[governorProperties.length];
            for (int i = 0; i < editTextPreferences.length; i++) {
                editTextPreferences[i] = new EditTextPreference(getActivity());
                editTextPreferences[i].setKey(governorProperties[i].getGovernorProperty());
                editTextPreferences[i].setTitle(governorProperties[i].getGovernorProperty());
                editTextPreferences[i].setSummary(governorProperties[i].getGovernorPropertyValue());
                editTextPreferences[i].setDialogTitle(governorProperties[i].getGovernorProperty());
                editTextPreferences[i].setDefaultValue(governorProperties[i].getGovernorPropertyValue());
                editTextPreferences[i].setOnPreferenceChangeListener(this);
                editTextPreferences[i].setPersistent(true);

                preferenceCategory.addPreference(editTextPreferences[i]);
            }
        }
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.getKey().equals("cpu_min_freq_pref")) {

        }
        if (preference.equals("cpu_max_freq_pref")) {

        }
        if (preference.equals("governor_pref")) {

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
