package com.rattlehead666.performancetweaker.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import com.rattlehead666.performancetweaker.app.R;

public class VirtualMemoryFragment extends PreferenceFragment {

  Context context;
  PreferenceCategory preferenceCategory;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.virtual_memory_preference);

    preferenceCategory = (PreferenceCategory) findPreference("vm_pref");

    context = getActivity();
  }
}
