package com.rattlehead666.performancetweaker.app.ui.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rattlehead666.performancetweaker.app.R;
import com.rattlehead666.performancetweaker.app.utils.VmUtils;
import java.util.LinkedHashMap;

public class VirtualMemoryFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener {

  Context context;
  PreferenceCategory preferenceCategory;
  EditTextPreference editTextPreferences[];

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_pref_container, container, false);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.virtual_memory_preference);

    preferenceCategory = (PreferenceCategory) findPreference("vm_pref");

    context = getActivity();
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    new PopulateVmEntries().execute();
  }

  @Override public boolean onPreferenceChange(Preference preference, Object newValue) {
    VmUtils.setVM(newValue.toString(), preference.getKey());
    preference.setSummary(newValue.toString());
    return true;
  }

  private class PopulateVmEntries extends AsyncTask<Void, Void, Void> {
    LinkedHashMap<String, String> vmEntries = new LinkedHashMap<>();

    @Override protected Void doInBackground(Void... params) {
      vmEntries = VmUtils.getVMfiles();
      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);

      if (vmEntries != null && vmEntries.size() != 0) {
        editTextPreferences = new EditTextPreference[vmEntries.size()];
        int i = 0;
        for (LinkedHashMap.Entry<String, String> entry : vmEntries.entrySet()) {
          editTextPreferences[i] = new EditTextPreference(context);

          editTextPreferences[i].setKey(entry.getKey());
          editTextPreferences[i].setTitle(entry.getKey());
          editTextPreferences[i].setSummary(entry.getValue());
          editTextPreferences[i].setDialogTitle(entry.getKey());
          editTextPreferences[i].setDefaultValue(entry.getValue());
          editTextPreferences[i].setOnPreferenceChangeListener(VirtualMemoryFragment.this);

          preferenceCategory.addPreference(editTextPreferences[i]);
          i++;
        }
      }
    }
  }
}
