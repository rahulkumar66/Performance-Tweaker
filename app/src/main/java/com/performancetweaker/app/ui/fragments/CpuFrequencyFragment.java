package com.performancetweaker.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.Constants;
import com.performancetweaker.app.utils.CpuFrequencyUtils;
import com.performancetweaker.app.utils.InterstialHelper;

public class CpuFrequencyFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {

    String[] availableFreqBigCore, availableFreqLittleCore;
    String[] availableGovernorsBigCore, availableGovernorsLittleCore;
    String maxFrequencyBig, minFrequencyBig, currentGovernorBig, maxFrequencyLittle, minFrequencyLittle, currentGovernorLittle;

    ListPreference bigCpuMaxFreqPreference, littleCpuMaxFreqPreference;
    ListPreference bigCpuMinFreqPreference, littleCpuMinFreqPreference;
    ListPreference bigGovernorPreference, littleGovernorPreference;
    Context context;
    ProgressBar progressBar;
    InterstialHelper fanInterstialHelper;
    private boolean isBigLittle = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.cpu_freq_preference, rootKey);
        context = getActivity().getBaseContext();
        bigCpuMaxFreqPreference = findPreference(Constants.PREF_CPU_MAX_FREQ);
        bigCpuMinFreqPreference = findPreference(Constants.PREF_CPU_MIN_FREQ);
        bigGovernorPreference = findPreference(Constants.PREF_CPU_GOV);
        isBigLittle = CpuFrequencyUtils.isBigLITTLE();
        if (isBigLittle) {
            littleCpuMaxFreqPreference = findPreference(Constants.PREF_CPU_MAX_LITTLE_FREQ);
            littleCpuMinFreqPreference = findPreference(Constants.PREF_CPU_MIN_LITTLE_FREQ);
            littleGovernorPreference = findPreference(Constants.PREF_CPU_LITTLE_GOV);
            availableFreqLittleCore = CpuFrequencyUtils.getAvailableFrequencies(4);
            availableGovernorsLittleCore = CpuFrequencyUtils.getAvailableGovernors(4);
        } else {
            PreferenceScreen cpuPrefScreen = findPreference("cpu_freq_pref");
            PreferenceCategory littleCoresCategory = findPreference("little_pref");
            cpuPrefScreen.removePreference(littleCoresCategory);
        }

        availableFreqBigCore = CpuFrequencyUtils.getAvailableFrequencies(0);
        availableGovernorsBigCore = CpuFrequencyUtils.getAvailableGovernors(0);

        bigCpuMaxFreqPreference.setOnPreferenceChangeListener(this);
        bigCpuMinFreqPreference.setOnPreferenceChangeListener(this);
        bigGovernorPreference.setOnPreferenceChangeListener(this);
        setHasOptionsMenu(true);

        populatePreferences();
        fanInterstialHelper = InterstialHelper.getInstance(getActivity());
        progressBar = getActivity().findViewById(R.id.loading_main);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePreferences();
        progressBar.setVisibility(View.GONE);
    }

    public void populatePreferences() {
        updateData();

        if (availableFreqBigCore != null) {
            bigCpuMaxFreqPreference.setEntries(CpuFrequencyUtils.toMhz(availableFreqBigCore));
            bigCpuMaxFreqPreference.setEntryValues(availableFreqBigCore);
            bigCpuMinFreqPreference.setEntries(CpuFrequencyUtils.toMhz(availableFreqBigCore));
            bigCpuMinFreqPreference.setEntryValues(availableFreqBigCore);
        }
        if (availableGovernorsBigCore != null) {
            bigGovernorPreference.setEntries(availableGovernorsBigCore);
            bigGovernorPreference.setEntryValues(availableGovernorsBigCore);
        }
        if (maxFrequencyBig != null && minFrequencyBig != null && currentGovernorBig != null) {
            updatePreferences();
        }

        if (isBigLittle) {
            if (availableFreqLittleCore != null) {
                littleCpuMaxFreqPreference.setEntries(CpuFrequencyUtils.toMhz(availableFreqLittleCore));
                littleCpuMaxFreqPreference.setEntryValues(availableFreqLittleCore);
                littleCpuMinFreqPreference.setEntries(CpuFrequencyUtils.toMhz(availableFreqLittleCore));
                littleCpuMinFreqPreference.setEntryValues(availableFreqLittleCore);
            }
            if (availableFreqLittleCore != null) {
                littleGovernorPreference.setEntries(availableGovernorsLittleCore);
                littleGovernorPreference.setEntryValues(availableGovernorsLittleCore);
            }
        }
    }

    public void updatePreferences() {
        bigCpuMaxFreqPreference.setValue(maxFrequencyBig);
        bigCpuMinFreqPreference.setValue(minFrequencyBig);
        bigGovernorPreference.setValue(currentGovernorBig);
        bigCpuMinFreqPreference.setSummary(CpuFrequencyUtils.toMhz(minFrequencyBig)[0]);
        bigCpuMaxFreqPreference.setSummary(CpuFrequencyUtils.toMhz(maxFrequencyBig)[0]);
        bigGovernorPreference.setSummary(currentGovernorBig);

        if (isBigLittle) {
            littleCpuMaxFreqPreference.setValue(maxFrequencyLittle);
            littleCpuMinFreqPreference.setValue(minFrequencyLittle);
            littleGovernorPreference.setValue(currentGovernorLittle);
            littleCpuMinFreqPreference.setSummary(CpuFrequencyUtils.toMhz(minFrequencyLittle)[0]);
            littleCpuMaxFreqPreference.setSummary(CpuFrequencyUtils.toMhz(maxFrequencyLittle)[0]);
            littleGovernorPreference.setSummary(currentGovernorLittle);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        fanInterstialHelper.showAd();
        if (preference.getKey().equals(Constants.PREF_CPU_MIN_FREQ)) {
            CpuFrequencyUtils.setMinFrequency(o.toString());
        }
        if (preference.getKey().equals(Constants.PREF_CPU_MAX_FREQ)) {
            CpuFrequencyUtils.setMaxFrequency(o.toString());
        }
        if (preference.getKey().equals(Constants.PREF_CPU_GOV)) {
            CpuFrequencyUtils.setGovernor(o.toString());
        }
        updateData();
        updatePreferences();
        return true;
    }

    public void updateData() {
        currentGovernorBig = CpuFrequencyUtils.getCurrentScalingGovernor(0);
        maxFrequencyBig = CpuFrequencyUtils.getCurrentMaxFrequency(0);
        minFrequencyBig = CpuFrequencyUtils.getCurrentMinFrequency(0);

        currentGovernorLittle = CpuFrequencyUtils.getCurrentScalingGovernor(4);
        maxFrequencyLittle = CpuFrequencyUtils.getCurrentMaxFrequency(4);
        minFrequencyLittle = CpuFrequencyUtils.getCurrentMinFrequency(4);
    }
}
