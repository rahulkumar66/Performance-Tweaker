package com.performancetweaker.app.ui.fragments;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.AdUtils;
import com.performancetweaker.app.utils.BuildPropUtils;

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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.util.LinkedHashMap;

public class BuildPropEditorFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    PreferenceCategory preferenceCategory;
    EditTextPreference editTextPreferences[];
    LinkedHashMap<String, String> buildPropEntries = new LinkedHashMap<>();

    MenuItem searchItem;
    Context context;

    ProgressBar progressBar;
    AdUtils adUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pref_container, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.loading_main);
        progressBar.setVisibility(View.VISIBLE);

        context = getActivity();
        adUtils = AdUtils.getInstance(context);
        adUtils.loadInterstialAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        new populateBuildPropEntries().execute();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        BuildPropUtils.overwrite(preference.getKey(), preference.getSummary().toString(),
                preference.getKey(), o.toString());
        preference.setSummary(o.toString());
        adUtils.showInterstialAd();
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.build_prop_menu, menu);

        searchItem = menu.findItem(R.id.search_build_prop);
        SearchView searchView = new SearchView(
                ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setActionView(searchItem, searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                preferenceCategory.removeAll();

                Object[] keys = buildPropEntries.keySet().toArray();
                Object[] values = buildPropEntries.values().toArray();

                for (int i = 0; i < keys.length; i++)
                    if (keys[i].toString().contains(newText)) {

                        EditTextPreference editTextPreferences = new EditTextPreference(context);

                        editTextPreferences.setKey(keys[i].toString());
                        editTextPreferences.setTitle(newText.isEmpty() ? keys[i].toString()
                                : Html.fromHtml(((String) keys[i]).replace(newText, "" +
                                "<b><font color=\"#2A7289\">" + newText + "</font></b>")));
                        editTextPreferences.setSummary(values[i].toString());
                        editTextPreferences.setDialogTitle(keys[i].toString());
                        editTextPreferences.setDefaultValue(values[i]);
                        editTextPreferences.setOnPreferenceChangeListener(BuildPropEditorFragment.this);

                        preferenceCategory.addPreference(editTextPreferences);
                    }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_entry_build_prop:
                editBuildPropDialog();
                break;
            case R.id.search_build_prop:
                MenuItemCompat.expandActionView(searchItem);
                break;
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
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            buildPropEntries = BuildPropUtils.getProps();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            addPreferencesFromResource(R.xml.build_prop_editor_fragment);

            preferenceCategory = (PreferenceCategory) findPreference("build_prop_pref");

            if (buildPropEntries != null && buildPropEntries.size() != 0) {
                progressBar.setVisibility(View.GONE);
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

