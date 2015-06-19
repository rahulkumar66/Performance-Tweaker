package com.phantomLord.cpufrequtils.app.ui;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.Constants;
import com.phantomLord.cpufrequtils.app.utils.IOUtils;


public class IOControlFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    String[] availableSchedulers, readAheadValues;
    String currentScheduler;
    String currentReadAhead;

    ListPreference IOScheduler;
    ListPreference ReadAheadCache;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        addPreferencesFromResource(R.xml.io_preferences);
        IOScheduler = (ListPreference) findPreference("disk_scheduler");
        ReadAheadCache = (ListPreference) findPreference("read_ahead_cache");

        IOScheduler.setOnPreferenceChangeListener(this);
        ReadAheadCache.setOnPreferenceChangeListener(this);

        populatePreferences();


    }

    private void populatePreferences() {
        availableSchedulers = IOUtils.getAvailableIOScheduler();
        currentScheduler = IOUtils.getCurrentIOScheduler();
        readAheadValues = Constants.readAheadKb;
        currentReadAhead = IOUtils.getReadAhead();

        if (availableSchedulers != null) {
            IOScheduler.setEntries(availableSchedulers);
            IOScheduler.setEntryValues(availableSchedulers);

            ReadAheadCache.setEntries(readAheadValues);
            ReadAheadCache.setEntryValues(readAheadValues);

        }
        if (currentScheduler != null) {
            IOScheduler.setDefaultValue(currentScheduler);
        }

        if (currentReadAhead != null) {
            ReadAheadCache.setDefaultValue(currentReadAhead);
        }

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.getKey().equals("disk_scheduler")) {

        }
        if (preference.getKey().equals("read_ahead_cache")) {

        }
        return true;
    }
}
