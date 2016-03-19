package com.rattlehead666.performancetweaker.app.ui.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.rattlehead666.performancetweaker.app.R;
import com.rattlehead666.performancetweaker.app.utils.CpuFrequencyUtils;

public class CpuFrequencyFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener {

  String[] availablefreq;
  String[] availableGovernors;
  String maxFrequency, minFrequency, currentGovernor;

  ListPreference CpuMaxFreqPreference;
  ListPreference CpuMinFreqPreference;
  ListPreference GovernorPreference;
  Preference preference;
  Context context;
  ProgressBar progressBar;
  Fragment f;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    progressBar = (ProgressBar) getActivity().findViewById(R.id.loading_main);
    return inflater.inflate(R.layout.fragment_pref_container, container, false);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    addPreferencesFromResource(R.xml.cpu_freq_preference);
    context = getActivity().getBaseContext();
    f = this;
    CpuMaxFreqPreference = (ListPreference) findPreference("cpu_max_freq_pref");
    CpuMinFreqPreference = (ListPreference) findPreference("cpu_min_freq_pref");
    GovernorPreference = (ListPreference) findPreference("governor_pref");
    preference = findPreference("governor_tune_pref");

    CpuMaxFreqPreference.setOnPreferenceChangeListener(this);
    CpuMinFreqPreference.setOnPreferenceChangeListener(this);
    GovernorPreference.setOnPreferenceChangeListener(this);
    preference.setOnPreferenceChangeListener(this);
    preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override public boolean onPreferenceClick(Preference preference) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter_anim, R.animator.exit_animation);
        fragmentTransaction.replace(R.id.main_content, new GovernorTuningFragment(),
            GovernorTuningFragment.TAG).commit();
        return true;
      }
    });
  }

  @Override public void onResume() {
    super.onResume();

    progressBar.setVisibility(View.VISIBLE);
    populatePreferences();
    progressBar.setVisibility(View.GONE);
  }

  public void populatePreferences() {

    updateData();

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
      updatePreferences();
    }
  }

  public void updatePreferences() {
    CpuMaxFreqPreference.setValue(maxFrequency);
    CpuMinFreqPreference.setValue(minFrequency);
    GovernorPreference.setValue(currentGovernor);

    CpuMinFreqPreference.setSummary((Integer.parseInt(minFrequency) / 1000) + " Mhz");
    CpuMaxFreqPreference.setSummary((Integer.parseInt(maxFrequency) / 1000) + " Mhz");
    GovernorPreference.setSummary(currentGovernor);
  }

  @Override public boolean onPreferenceChange(Preference preference, Object o) {
    if (preference.getKey().equals("cpu_min_freq_pref")) {
      CpuFrequencyUtils.setMinFrequency(o.toString(), context);
    }
    if (preference.getKey().equals("cpu_max_freq_pref")) {
      CpuFrequencyUtils.setMaxFrequency(o.toString(), context);
    }
    if (preference.getKey().equals("governor_pref")) {
      CpuFrequencyUtils.setGovernor(o.toString(), context);
    }
    updateData();
    updatePreferences();
    return true;
  }

  public void updateData() {
    availablefreq = CpuFrequencyUtils.getAvailableFrequencies();
    availableGovernors = CpuFrequencyUtils.getAvailableGovernors();
    currentGovernor = CpuFrequencyUtils.getCurrentScalingGovernor();
    maxFrequency = CpuFrequencyUtils.getCurrentMaxFrequency();
    minFrequency = CpuFrequencyUtils.getCurrentMinFrequency();
  }
}
