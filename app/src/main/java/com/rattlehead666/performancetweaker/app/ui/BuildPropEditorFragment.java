package com.rattlehead666.performancetweaker.app.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import com.rattlehead666.performancetweaker.app.R;
import com.rattlehead666.performancetweaker.app.utils.BuildPropUtils;
import java.util.LinkedHashMap;

public class BuildPropEditorFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener {

  PreferenceCategory preferenceCategory;
  EditTextPreference editTextPreferences[];

  Context context;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.build_prop_editor_fragment);

    preferenceCategory = (PreferenceCategory) findPreference("build_prop_pref");

    context = getActivity();
    new populateBuildPropEntries().execute();
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override public boolean onPreferenceChange(Preference preference, Object o) {
    BuildPropUtils.overwrite(preference.getKey(), preference.getSummary().toString(),
        preference.getKey(), o.toString());

    preference.setSummary(o.toString());
    return true;
  }

  private class populateBuildPropEntries extends AsyncTask<Void, Void, Void> {

    LinkedHashMap<String, String> buildPropEntries = new LinkedHashMap<>();

    @Override protected Void doInBackground(Void... voids) {
      buildPropEntries = BuildPropUtils.getProps();
      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      int i = 0;
      if (buildPropEntries != null && buildPropEntries.size() != 0) {
        editTextPreferences = new EditTextPreference[buildPropEntries.size()];
        for (LinkedHashMap.Entry<String, String> entry : buildPropEntries.entrySet()) {
          editTextPreferences[i] = new EditTextPreference(context);
          editTextPreferences[i].setKey(entry.getKey());
          editTextPreferences[i].setTitle(entry.getKey());
          editTextPreferences[i].setSummary(entry.getValue());
          editTextPreferences[i].setDialogTitle(entry.getKey());
          editTextPreferences[i].setDefaultValue(entry.getValue());
          editTextPreferences[i].setOnPreferenceChangeListener(BuildPropEditorFragment.this);

          preferenceCategory.addPreference(editTextPreferences[i]);
          i++;
        }
      }
    }
  }
}
