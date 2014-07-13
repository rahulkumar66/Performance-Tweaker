package com.phantomLord.cpufrequtils.app.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.MiscUtils;

public class PreferenceActivity extends SherlockPreferenceActivity implements
		OnPreferenceChangeListener {
	ListPreference listPrefs;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String key = mPrefs.getString("listPref", "");
		this.setTheme(MiscUtils.THEMES_MAP.get(key));
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preference);
		listPrefs = (ListPreference) findPreference("listPref");
		listPrefs.setOnPreferenceChangeListener(this);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {

		return false;
	}
}
