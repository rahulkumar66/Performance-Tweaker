package com.rattlehead666.performancetweaker.app.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import com.rattlehead666.performancetweaker.app.R;
import com.rattlehead666.performancetweaker.app.utils.BuildPropUtils;
import java.util.LinkedHashMap;



public class BuildPropEditorFragment extends PreferenceFragment
    implements Preference.OnPreferenceChangeListener {

  PreferenceCategory preferenceCategory;
  EditTextPreference editTextPreferences[];

  SwipeRefreshLayout refreshLayout;

  MenuItem searchItem;

  Context context;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_pref_container, container, false);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    addPreferencesFromResource(R.xml.build_prop_editor_fragment);

    preferenceCategory = (PreferenceCategory) findPreference("build_prop_pref");

    context = getActivity();
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    new populateBuildPropEntries().execute();
  }

  @Override public boolean onPreferenceChange(Preference preference, Object o) {
    BuildPropUtils.overwrite(preference.getKey(), preference.getSummary().toString(),
        preference.getKey(), o.toString());
    preference.setSummary(o.toString());
    return true;
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.build_prop_menu, menu);

    searchItem = menu.findItem(R.id.search_build_prop);
    SearchView searchView = new SearchView(((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext());
    MenuItemCompat.setActionView(searchItem, searchView);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.add_entry_build_prop:
        editBuildPropDialog();
    }
    return super.onOptionsItemSelected(item);
  }

  private void editBuildPropDialog() {

    Activity activity = getActivity();
    final View editDialog =
        LayoutInflater.from(getActivity()).inflate(R.layout.dialog_build_prop, null, false);
    final EditText etName = (EditText) editDialog.findViewById(R.id.prop_name);
    final EditText etValue = (EditText) editDialog.findViewById(R.id.prop_value);

    new AlertDialog.Builder(activity).setView(editDialog)
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
              }
            })
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            if (etValue.getText() != null && etName.getText() != null) {
              BuildPropUtils.addKey(etName.getText().toString().trim(),
                  etValue.getText().toString().trim());
            } else {
              return;
            }
          }
        })
        .show();
  }

  private class populateBuildPropEntries extends AsyncTask<Void, Void, Void> {

    LinkedHashMap<String, String> buildPropEntries = new LinkedHashMap<>();

    @Override protected Void doInBackground(Void... voids) {
      buildPropEntries = BuildPropUtils.getProps();
      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);

      if (buildPropEntries != null && buildPropEntries.size() != 0) {
        editTextPreferences = new EditTextPreference[buildPropEntries.size()];
        int i = 0;
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

