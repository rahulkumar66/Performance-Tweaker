package com.performancetweaker.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.FANInterstialHelper;
import com.performancetweaker.app.utils.GpuUtils;

import java.util.ArrayList;

public class SettingsFragment extends PreferenceFragmentCompat {

    MultiSelectListPreference mMultiSelectListPreference;
    GpuUtils gpuUtils;
    FANInterstialHelper fanInterstialHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pref_container, container, false);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

        gpuUtils = GpuUtils.getInstance();
        mMultiSelectListPreference = findPreference("select_apply_on_boot");

        ArrayList<CharSequence> entries = new ArrayList<>();
        entries.add(getString(R.string.cpu_frequency));
        if (gpuUtils.isGpuFrequencyScalingSupported()) {
            entries.add(getString(R.string.gpu_frequency));
        }
//        entries.add(getString(R.string.io));
//        entries.add(getString(R.string.build_prop));
//        entries.add(getString(R.string.vm));

        CharSequence[] charSequences = new CharSequence[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            charSequences[i] = entries.get(i);
        }
        mMultiSelectListPreference.setEntries(charSequences);
        mMultiSelectListPreference.setEntryValues(charSequences);
        mMultiSelectListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return true;
            }
        });
        fanInterstialHelper = FANInterstialHelper.getInstance(getActivity());
    }
}
