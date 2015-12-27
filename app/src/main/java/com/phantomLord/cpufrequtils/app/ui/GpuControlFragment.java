package com.phantomLord.cpufrequtils.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.GpuUtils;

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

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.gpu_preferences);

    context = getActivity().getBaseContext();

    maxGpuFrequencyPreference = (ListPreference) findPreference("gpu_max_freq_pref");
    minGpuFrequencyPreference = (ListPreference) findPreference("gpu_min_freq_pref");
    availableGpuGovernorPreference = (ListPreference) findPreference("gpu_governor_pref");

    availableGpuFrequencies = GpuUtils.getAvailableGpuFrequencies();
    availableGpuGovernors = GpuUtils.getAvailableGpuGovernors();

    populatePreferences();

    maxGpuFrequencyPreference.setOnPreferenceChangeListener(this);
    minGpuFrequencyPreference.setOnPreferenceChangeListener(this);
    availableGpuGovernorPreference.setOnPreferenceChangeListener(this);
  }

  @Override public void onResume() {
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
    maxGpuFrequencyPreference.setSummary((Integer.parseInt(maxFrequency) / (1000 * 1000) + "Mhz"));

    minGpuFrequencyPreference.setValue(minFrequency);
    minGpuFrequencyPreference.setSummary((Integer.parseInt(minFrequency) / (1000 * 1000) + "Mhz"));

    availableGpuGovernorPreference.setValue(currentGovernor);
    availableGpuGovernorPreference.setSummary(currentGovernor);
  }

  @Override public boolean onPreferenceChange(Preference preference, Object value) {

    if (preference.getKey().equals("gpu_max_freq_pref")) {
      GpuUtils.setMaxGpuFrequency(value.toString(), getActivity().getBaseContext());
    } else if (preference.getKey().equals("gpu_min_freq_pref")) {
      GpuUtils.setMinFrequency(value.toString(), getActivity().getBaseContext());
    } else if (preference.getKey().equals("gpu_governor_pref")) {
      GpuUtils.setGpuFrequencyScalingGovernor(value.toString(), getActivity().getBaseContext());
    }

    updateData();
    updatePreferences();

    return true;
  }
}
