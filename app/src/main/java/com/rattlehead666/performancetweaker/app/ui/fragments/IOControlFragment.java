package com.rattlehead666.performancetweaker.app.ui.fragments;

import com.rattlehead666.performancetweaker.app.R;
import com.rattlehead666.performancetweaker.app.utils.Constants;
import com.rattlehead666.performancetweaker.app.utils.IOUtils;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IOControlFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    String[] availableSchedulers, readAheadValues;
    String currentScheduler;
    String currentReadAhead;

    ListPreference IOScheduler;
    ListPreference ReadAheadCache;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pref_container, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addPreferencesFromResource(R.xml.io_preferences);
        IOScheduler = (ListPreference) findPreference("disk_scheduler");
        ReadAheadCache = (ListPreference) findPreference("read_ahead_cache");

        context = getActivity().getBaseContext();

        populatePreferences();

        IOScheduler.setOnPreferenceChangeListener(this);
        ReadAheadCache.setOnPreferenceChangeListener(this);
    }

    private void populatePreferences() {

        updateData();

        if (availableSchedulers != null) {
            IOScheduler.setEntries(availableSchedulers);
            IOScheduler.setEntryValues(availableSchedulers);

            ReadAheadCache.setEntries(readAheadValues);
            ReadAheadCache.setEntryValues(readAheadValues);
        }
        if (currentScheduler != null) {
            IOScheduler.setValue(currentScheduler);
            IOScheduler.setSummary(currentScheduler);
        }

        if (currentReadAhead != null) {
            ReadAheadCache.setValue(currentReadAhead);
            ReadAheadCache.setSummary(currentReadAhead);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {

        if (preference.getKey().equals("disk_scheduler")) {
            IOUtils.setDiskScheduler(o.toString(), context);
        }
        if (preference.getKey().equals("read_ahead_cache")) {
            IOUtils.setReadAhead(o.toString(), context);
        }

        updateData();
        updatePreferences();
        return true;
    }

    public void updateData() {
        availableSchedulers = IOUtils.getAvailableIOScheduler();
        currentScheduler = IOUtils.getCurrentIOScheduler();
        readAheadValues = Constants.readAheadKb;
        currentReadAhead = IOUtils.getReadAhead();
    }

    public void updatePreferences() {
        IOScheduler.setSummary(currentScheduler);
        ReadAheadCache.setSummary(currentReadAhead);
    }
}
