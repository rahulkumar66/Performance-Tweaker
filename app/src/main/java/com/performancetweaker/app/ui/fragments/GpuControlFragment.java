package com.performancetweaker.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.Constants;
import com.performancetweaker.app.utils.GpuUtils;

public class GpuControlFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    ListPreference maxGpuFrequencyPreference;
    ListPreference minGpuFrequencyPreference;
    ListPreference availableGpuGovernorPreference;
    Context context;

    String[] availableGpuFrequencies;
    String[] availableGpuGovernors;
    String maxFrequency;
    String minFrequency;
    String currentGovernor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pref_container, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.gpu_preferences);

        context = getActivity().getBaseContext();

        maxGpuFrequencyPreference = (ListPreference) findPreference(Constants.PREF_GPU_MAX);
        minGpuFrequencyPreference = (ListPreference) findPreference(Constants.PREF_GPU_MIN);
        availableGpuGovernorPreference = (ListPreference) findPreference(Constants.PREF_GPU_GOV);

        availableGpuFrequencies = GpuUtils.getAvailableGpuFrequencies();
        availableGpuGovernors = GpuUtils.getAvailableGpuGovernors();

        populatePreferences();

        maxGpuFrequencyPreference.setOnPreferenceChangeListener(this);
        minGpuFrequencyPreference.setOnPreferenceChangeListener(this);
        availableGpuGovernorPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePreferences();
    }

    private void populatePreferences() {
        updateData();

        if (availableGpuFrequencies != null && availableGpuFrequencies.length != 0) {

            maxGpuFrequencyPreference.setEntries(GpuUtils.toMhz(availableGpuFrequencies));
            maxGpuFrequencyPreference.setEntryValues(availableGpuFrequencies);

            minGpuFrequencyPreference.setEntries(GpuUtils.toMhz(availableGpuFrequencies));
            minGpuFrequencyPreference.setEntryValues(availableGpuFrequencies);
        }

        if (availableGpuGovernors != null && availableGpuGovernors.length != 0) {
            availableGpuGovernorPreference.setEntries(availableGpuGovernors);
            availableGpuGovernorPreference.setEntryValues(availableGpuGovernors);
        }

        if (maxFrequency != null && minFrequency != null) {
            updatePreferences();
        }
    }

    public void updateData() {
        maxFrequency = GpuUtils.getMaxGpuFrequency();
        minFrequency = GpuUtils.getMinGpuFrequency();
        currentGovernor = GpuUtils.getCurrentGpuGovernor();
    }

    public void updatePreferences() {
        maxGpuFrequencyPreference.setValue(maxFrequency);
        maxGpuFrequencyPreference.setSummary(GpuUtils.toMhz(maxFrequency)[0]);

        minGpuFrequencyPreference.setValue(minFrequency);
        minGpuFrequencyPreference.setSummary(GpuUtils.toMhz(minFrequency)[0]);

        availableGpuGovernorPreference.setValue(currentGovernor);
        availableGpuGovernorPreference.setSummary(currentGovernor);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        if (preference.getKey().equals(Constants.PREF_GPU_MAX)) {
            GpuUtils.setMaxGpuFrequency(value.toString(), getActivity().getBaseContext());
        } else if (preference.getKey().equals(Constants.PREF_GPU_MIN)) {
            GpuUtils.setMinFrequency(value.toString(), getActivity().getBaseContext());
        } else if (preference.getKey().equals(Constants.PREF_GPU_GOV)) {
            GpuUtils.setGpuFrequencyScalingGovernor(value.toString(), getActivity()
                    .getBaseContext());
        }

        updateData();
        updatePreferences();
        return true;
    }
}
