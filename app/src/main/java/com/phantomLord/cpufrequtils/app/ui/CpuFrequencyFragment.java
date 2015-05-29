package com.phantomLord.cpufrequtils.app.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phantomLord.cpufrequtils.app.utils.SysUtils;
import com.stericson.RootTools.RootTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CpuFrequencyFragment extends Fragment implements
        ActionBar.OnNavigationListener {
    //ArrayWheelAdapter<String> frequencyAdapter;
    //ArrayWheelAdapter<String> governorAdapter;

    List<String> availableFrequencies = new ArrayList<String>();
    List<String> availableGovernors = new ArrayList<String>();

    String[] availablefreq;
    String[] availableScalingGovernors;
    String maxFrequency, minFrequency, currentGovernor;

    View mView;
    ActionBar actionBar;

    //AbstractWheel maxFreq, minimum, governor;

    Context themedContext, context;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        availablefreq = SysUtils.getAvailableFrequencies();
        availableFrequencies = Arrays.asList(availablefreq);
        availableScalingGovernors = SysUtils.getAvailableGovernors();
        availableGovernors = Arrays.asList(availableScalingGovernors);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (RootTools.isAccessGiven()) {

            updateValues();
            //	mView = inflater.inflate(R.layout.cpu_control_fragment, container,
            //			false);
            /*
            Button applyButton = (Button) mView.findViewById(R.id.button_apply);
			context = mView.getContext();
			maxFreq = (AbstractWheel) mView.findViewById(R.id.mins);
			minimum = (AbstractWheel) mView
					.findViewById(R.id.minimumfreq_spinner);
			governor = (AbstractWheel) mView
					.findViewById(R.id.governor_spinner);

			ArrayWheelAdapter<String> minAdapter = new ArrayWheelAdapter<String>(
					context, SysUtils.toMhz(availablefreq));
			minAdapter.setItemResource(R.layout.spinner_wheel_box_layout);
			minAdapter.setItemTextResource(R.id.text);
			maxFreq.setViewAdapter(minAdapter);
			maxFreq.setCurrentItem(availableFrequencies.indexOf(maxFrequency));

			minimum.setViewAdapter(minAdapter);
			minimum.setCurrentItem(availableFrequencies.indexOf(minFrequency));

			ArrayWheelAdapter<String> governorAdapter = new ArrayWheelAdapter<String>(
					context, availableScalingGovernors);
			governorAdapter.setItemResource(R.layout.spinner_wheel_box_layout);
			governorAdapter.setItemTextResource(R.id.text);
			governor.setViewAdapter(governorAdapter);
			governor.setCurrentItem(availableGovernors.indexOf(currentGovernor));

			applyButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (SysUtils.isRooted()) {
						String max = availableFrequencies.get(maxFreq
								.getCurrentItem());
						String min = availableFrequencies.get(minimum
								.getCurrentItem());
						String gov = availableGovernors.get(governor
								.getCurrentItem());
						SysUtils.setFrequencyAndGovernor(max, min, gov,
								mView.getContext());
						updateValues();
						prefs = PreferenceManager
								.getDefaultSharedPreferences(context);
						boolean applyOnBoot = prefs.getBoolean(
								Constants.PREF_CPU_APPLY_ON_BOOT, false);
						if (applyOnBoot) {
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString(Constants.PREF_MAX_FREQ, max);
							editor.putString(Constants.PREF_MIN_FREQ, min);
							editor.putString(Constants.PREF_GOV, gov);
							editor.commit();
						}
					} else {
						new RootNotFoundAlertDialog().show(
								getFragmentManager(), Constants.App_Tag);
					}
				}
			});
			*/
        }

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        themedContext = actionBar.getThemedContext();
        /*
        if (!(SysUtils.getCoreCount() == 1)) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setListNavigationCallbacks(
					new CpuControlActionBarSpinner(themedContext), this);
		}
		*/
    }

    @Override
    public void onDestroyView() {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onDestroyView();
    }

    private void updateValues() {
        maxFrequency = SysUtils.getCurrentMaxFrequeny();
        minFrequency = SysUtils.getCurrentMinFrequency();
        currentGovernor = SysUtils.getCurrentScalingGovernor();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        switch (itemPosition) {
            default:
                break;
        }
        return false;
    }
}
