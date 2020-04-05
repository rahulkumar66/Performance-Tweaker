package com.performancetweaker.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.Constants;
import com.performancetweaker.app.utils.FANInterstialHelper;
import com.performancetweaker.app.utils.IOUtils;

public class IOControlFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    String[] availableSchedulers, readAheadValues;
    String currentScheduler;
    String currentReadAhead;

    ListPreference IOScheduler;
    ListPreference ReadAheadCache;
    Context context;
    FANInterstialHelper fanInterstialHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pref_container, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addPreferencesFromResource(R.xml.io_preferences);
        IOScheduler = (ListPreference) findPreference(Constants.PREF_IO_SCHEDULER);
        ReadAheadCache = (ListPreference) findPreference(Constants.PREF_READ_AHEAD);

        context = getActivity().getBaseContext();

        populatePreferences();

        IOScheduler.setOnPreferenceChangeListener(this);
        ReadAheadCache.setOnPreferenceChangeListener(this);
        fanInterstialHelper = FANInterstialHelper.getInstance(getActivity());
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

        if (preference.getKey().equals(Constants.PREF_IO_SCHEDULER)) {
            IOUtils.setDiskScheduler(o.toString(), context);
        }
        if (preference.getKey().equals(Constants.PREF_READ_AHEAD)) {
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
        fanInterstialHelper.showAd();
        IOScheduler.setSummary(currentScheduler);
        ReadAheadCache.setSummary(currentReadAhead);
    }
}
