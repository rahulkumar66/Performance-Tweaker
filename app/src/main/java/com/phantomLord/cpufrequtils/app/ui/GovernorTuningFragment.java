package com.phantomLord.cpufrequtils.app.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.CpuFrequencyUtils;
import com.phantomLord.cpufrequtils.app.utils.GovernorProperties;

public class GovernorTuningFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    PreferenceCategory preferenceCategory;
    EditTextPreference editTextPreferences[];
    GovernorProperties[] governorProperties;

    FrameLayout governorPropertiesContainer;

    Context context;

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.governor_tuning_preferences);

        preferenceCategory = (PreferenceCategory) findPreference("governor_tune_pref");
        governorPropertiesContainer = (FrameLayout) getActivity()
                .findViewById(R.id.frame_layout_preference);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.loading);
        context = getActivity();

        new GetGovernorPropertiesTask().execute();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return true;
    }

    private class GetGovernorPropertiesTask extends AsyncTask<Void, Void, GovernorProperties[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            governorPropertiesContainer.setVisibility(View.GONE);
        }

        @Override
        protected GovernorProperties[] doInBackground(Void... params) {
            governorProperties = CpuFrequencyUtils.getGovernorProperties();
            return governorProperties;
        }

        @Override
        protected void onPostExecute(GovernorProperties[] governorProperties) {
            super.onPostExecute(governorProperties);

            progressBar.setVisibility(View.GONE);
            governorPropertiesContainer.setVisibility(View.VISIBLE);

            if (governorProperties != null && governorProperties.length != 0) {
                editTextPreferences = new EditTextPreference[governorProperties.length];
                for (int i = 0; i < editTextPreferences.length; i++) {
                    editTextPreferences[i] = new EditTextPreference(context);
                    editTextPreferences[i].setKey(governorProperties[i].getGovernorProperty());
                    editTextPreferences[i].setTitle(governorProperties[i].getGovernorProperty());
                    editTextPreferences[i].setSummary(governorProperties[i].getGovernorPropertyValue());
                    editTextPreferences[i].setDialogTitle(governorProperties[i].getGovernorProperty());
                    editTextPreferences[i].setDefaultValue(governorProperties[i].getGovernorPropertyValue());
                    //   editTextPreferences[i].setOnPreferenceChangeListener();
                    editTextPreferences[i].setPersistent(true);

                    preferenceCategory.addPreference(editTextPreferences[i]);
                }
            }
        }
    }

}
