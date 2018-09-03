package com.performancetweaker.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.Constants;
import com.performancetweaker.app.utils.CpuFrequencyUtils;


public class CpuFrequencyFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    String[] availablefreq;
    String[] availableGovernors;
    String maxFrequency, minFrequency, currentGovernor;

    ListPreference CpuMaxFreqPreference;
    ListPreference CpuMinFreqPreference;
    ListPreference GovernorPreference;
  //  Preference preference;
    Context context;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressBar = getActivity().findViewById(R.id.loading_main);
        progressBar.setVisibility(View.VISIBLE);
        return inflater.inflate(R.layout.fragment_pref_container, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.cpu_freq_preference);
        context = getActivity().getBaseContext();

        CpuMaxFreqPreference = (ListPreference) findPreference(Constants.PREF_CPU_MAX_FREQ);
        CpuMinFreqPreference = (ListPreference) findPreference(Constants.PREF_CPU_MIN_FREQ);
        GovernorPreference = (ListPreference) findPreference(Constants.PREF_CPU_GOV);
       // preference = findPreference("governor_tune_pref");

        availablefreq = CpuFrequencyUtils.getAvailableFrequencies();
        availableGovernors = CpuFrequencyUtils.getAvailableGovernors();
        populatePreferences();

        CpuMaxFreqPreference.setOnPreferenceChangeListener(this);
        CpuMinFreqPreference.setOnPreferenceChangeListener(this);
        GovernorPreference.setOnPreferenceChangeListener(this);
//        preference.setOnPreferenceChangeListener(this);
//        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(R.animator.enter_anim, R.animator.exit_animation);
//                fragmentTransaction.replace(R.id.main_content, new GovernorTuningFragment(),
//                        GovernorTuningFragment.TAG).commit();
//                return true;
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePreferences();
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

        CpuMinFreqPreference.setSummary(CpuFrequencyUtils.toMhz(minFrequency)[0]);
        CpuMaxFreqPreference.setSummary(CpuFrequencyUtils.toMhz(maxFrequency)[0]);
        GovernorPreference.setSummary(currentGovernor);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.getKey().equals(Constants.PREF_CPU_MIN_FREQ)) {
            CpuFrequencyUtils.setMinFrequency(o.toString(), context);
        }
        if (preference.getKey().equals(Constants.PREF_CPU_MAX_FREQ)) {
            CpuFrequencyUtils.setMaxFrequency(o.toString(), context);
        }
        if (preference.getKey().equals(Constants.PREF_CPU_GOV)) {
            CpuFrequencyUtils.setGovernor(o.toString(), context);
        }
        updateData();
        updatePreferences();
        return true;
    }

    public void updateData() {
        currentGovernor = CpuFrequencyUtils.getCurrentScalingGovernor();
        maxFrequency = CpuFrequencyUtils.getCurrentMaxFrequency();
        minFrequency = CpuFrequencyUtils.getCurrentMinFrequency();
    }
}
