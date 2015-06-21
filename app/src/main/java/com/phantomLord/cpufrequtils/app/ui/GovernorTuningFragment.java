package com.phantomLord.cpufrequtils.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.CpuFrequencyUtils;
import com.phantomLord.cpufrequtils.app.utils.GovernorProperties;

public class GovernorTuningFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    PreferenceCategory preferenceCategory;
    EditTextPreference editTextPreferences[];
    GovernorProperties[] governorProperties;

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.governor_tuning_preferences);
        preferenceCategory = (PreferenceCategory) findPreference("governor_tune_pref");
        context = getActivity();

        governorProperties = CpuFrequencyUtils.getGovernorProperties();
        if (governorProperties != null && governorProperties.length != 0) {
            editTextPreferences = new EditTextPreference[governorProperties.length];
            for (int i = 0; i < editTextPreferences.length; i++) {
                editTextPreferences[i] = new EditTextPreference(context);
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
        Log.d("" + preference, o + "");
        return true;
    }


}
