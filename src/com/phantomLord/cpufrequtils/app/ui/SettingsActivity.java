package com.phantomLord.cpufrequtils.app.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.phantomLord.cpufrequtils.app.R;

public class SettingsActivity extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
	}
}
