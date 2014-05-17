package com.phantomLord.cpufrequtils.app.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.dialogs.RootNotFoundAlertDialog;
import com.phantomLord.cpufrequtils.app.utils.Constants;
import com.phantomLord.cpufrequtils.app.utils.CpuUtils;
import com.phantomLord.cpufrequtils.app.utils.RootUtils;

public class CpuFrequencyFragment extends SherlockFragment {
	ArrayWheelAdapter<String> frequencyAdapter;
	ArrayWheelAdapter<String> governorAdapter;

	List<String> availableFrequencies=new ArrayList<String>();
	List<String> availableGovernors=new ArrayList<String>();

	String[] availablefreq;
	String[] availableScalingGovernors;
	String maxFrequency, minFrequency, currentGovernor;

	View mView;

	AbstractWheel maxFreq, minimum, governor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		availablefreq = CpuUtils.getAvailableFrequencies();
		availableFrequencies=Arrays.asList(availablefreq);

		availableScalingGovernors = CpuUtils.getAvailableGovernors();
		availableGovernors=Arrays.asList(availableScalingGovernors);

		updateValues();

	}

	private void updateValues() {
		maxFrequency = CpuUtils.getCurrentMaxFrequeny();

		minFrequency = CpuUtils.getCurrentMinFrequency();

		currentGovernor = CpuUtils.getCurrentScalingGovernor();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.cpu_control_fragment, container,
				false);
		Button applyButton = (Button) mView.findViewById(R.id.button_apply);
		Context context = mView.getContext();
		maxFreq = (AbstractWheel) mView.findViewById(R.id.mins);
		minimum = (AbstractWheel) mView.findViewById(R.id.minimumfreq_spinner);
		governor = (AbstractWheel) mView.findViewById(R.id.governor_spinner);

		ArrayWheelAdapter<String> minAdapter = new ArrayWheelAdapter<String>(
				context, CpuUtils.toMhz(availablefreq));
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

				if (RootUtils.isRooted()) {
					CpuUtils.setFrequencyAndGovernor(
							availableFrequencies.get(maxFreq.getCurrentItem()),
							availableFrequencies.get(minimum.getCurrentItem()),
							availableGovernors.get(governor.getCurrentItem()), mView.getContext());
					updateValues();

				} else {
					new RootNotFoundAlertDialog().show(getFragmentManager(),
							Constants.App_Tag);
				}
			}
		});
		return mView;
	}
}
