package com.phantomLord.cpufrequtils.app.ui;

import android.os.Bundle;

import com.phantomLord.cpufrequtils.app.R;

public class SettingsFragment extends
		android.support.v4.preference.PreferenceFragment {

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		addPreferencesFromResource(R.xml.preference);
	}

}
