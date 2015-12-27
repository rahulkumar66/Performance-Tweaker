package com.phantomLord.cpufrequtils.app.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.phantomLord.cpufrequtils.app.R;

public class SettingsFragment extends PreferenceFragment {

  @Override public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    addPreferencesFromResource(R.xml.preference);
  }
}
