package com.performancetweaker.app.ui.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.CpuFrequencyUtils;
import com.performancetweaker.app.utils.GovernorProperty;

public class GovernorTuningFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, SwipeRefreshLayout.OnRefreshListener {

    public static String TAG = "GOVERNOR_TUNING";
    PreferenceCategory preferenceCategory;
    EditTextPreference editTextPreferences[];
    GovernorProperty[] governorProperties;
    Context context;
    View view;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pref_refresh_container, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addPreferencesFromResource(R.xml.governor_tuning_preferences);

        preferenceCategory = (PreferenceCategory) findPreference("governor_tune_pref");
        context = getActivity();

        swipeRefreshLayout.setOnRefreshListener(this);

        //We are posting SwipeRefreshLayout it to a time in the future
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                new GetGovernorPropertiesTask().execute();
            }
        }, 500);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        CpuFrequencyUtils.setGovernorProperty(new GovernorProperty(preference.getKey(), o.toString()),
                getActivity());
        preference.setSummary(o.toString());
        return true;
    }

    @Override
    public void onRefresh() {
        new GetGovernorPropertiesTask().execute();
    }

    private class GetGovernorPropertiesTask extends AsyncTask<Void, Void, GovernorProperty[]> {

        @Override
        protected GovernorProperty[] doInBackground(Void... params) {
            governorProperties = CpuFrequencyUtils.getGovernorProperties();
            return governorProperties;
        }

        @Override
        protected void onPostExecute(GovernorProperty[] governorProperties) {
            super.onPostExecute(governorProperties);

            if (governorProperties != null && governorProperties.length != 0) {
                editTextPreferences = new EditTextPreference[governorProperties.length];
                for (int i = 0; i < editTextPreferences.length; i++) {
                    editTextPreferences[i] = new EditTextPreference(context);
                    editTextPreferences[i].setKey(governorProperties[i].getGovernorProperty());
                    editTextPreferences[i].setTitle(governorProperties[i].getGovernorProperty());
                    editTextPreferences[i].setSummary(governorProperties[i].getGovernorPropertyValue());
                    editTextPreferences[i].setDialogTitle(governorProperties[i].getGovernorProperty());
                    editTextPreferences[i].setDefaultValue(governorProperties[i].getGovernorPropertyValue());
                    editTextPreferences[i].setOnPreferenceChangeListener(GovernorTuningFragment.this);

                    preferenceCategory.addPreference(editTextPreferences[i]);
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
