package com.performancetweaker.app.ui.fragments;

import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.AdUtils;
import com.performancetweaker.app.utils.Constants;
import com.performancetweaker.app.utils.GpuUtils;
import com.performancetweaker.app.utils.SystemAppUtilities;

import java.util.ArrayList;

public class SettingsFragment extends PreferenceFragment {

    MultiSelectListPreference mMultiSelectListPreference;
    Preference uninstallSystemAppButton;
    GpuUtils gpuUtils;
    AdUtils adUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pref_container, container, false);
    }

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.settings_preferences);

        gpuUtils = GpuUtils.getInstance();

        final PreferenceCategory preferenceCategory = (PreferenceCategory) findPreference("settings_category");
        mMultiSelectListPreference = (MultiSelectListPreference) findPreference("select_apply_on_boot");
        uninstallSystemAppButton = findPreference("uninstall_system_app");
        uninstallSystemAppButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Boolean status = SystemAppUtilities.uninstallAsSystemApp(Constants.APK_NAME);
                if(status) {
                    if(!SystemAppUtilities.isSystemApp(Constants.APK_NAME)) {
                        preferenceCategory.removePreference(uninstallSystemAppButton);
                    }
                    Toast.makeText(getActivity(),"Successfully uninstalled app from system\n Please reboot for changes to take place",Toast.LENGTH_LONG);
                }else {
                    Toast.makeText(getActivity(),"Unknown error occurred while uninstalling app",Toast.LENGTH_SHORT);
                }
                adUtils.showInterstialAd();
                return true;
            }
        });

        ArrayList<CharSequence> entries = new ArrayList<>();
        entries.add(getString(R.string.cpu_frequency));
        if (gpuUtils.isGpuFrequencyScalingSupported()) {
            entries.add(getString(R.string.gpu_frequency));
        }
        entries.add(getString(R.string.io));
        entries.add(getString(R.string.build_prop));
        entries.add(getString(R.string.vm));

        CharSequence[] charSequences = new CharSequence[entries.size()];
        for (int i = 0; i < entries.size(); i++) {
            charSequences[i] = entries.get(i);
        }
        mMultiSelectListPreference.setEntries(charSequences);
        mMultiSelectListPreference.setEntryValues(charSequences);

        if(!SystemAppUtilities.isSystemApp(Constants.APK_NAME)) {
            preferenceCategory.removePreference(uninstallSystemAppButton);
        }
        adUtils = AdUtils.getInstance(getActivity());
        adUtils.loadInterstialAd();
    }
}
