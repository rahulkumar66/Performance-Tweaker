package com.performancetweaker.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.CPUHotplugUtils;
import com.performancetweaker.app.utils.Constants;

public class CpuHotplugFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    Context context;
    SwitchPreference switchPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pref_container, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_hotplug_preferences);
        switchPreference = (SwitchPreference) findPreference(Constants.PREF_HOTPLUG);
        context = getActivity().getBaseContext();
    }

    @Override
    public void onResume() {
        super.onResume();

        switchPreference.setChecked(CPUHotplugUtils.isMpdecisionActive());
        switchPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        CPUHotplugUtils.activateMpdecision(Boolean.parseBoolean(newValue.toString()));
        switchPreference.setChecked(CPUHotplugUtils.isMpdecisionActive());
        return Boolean.parseBoolean(newValue.toString());
    }
}
