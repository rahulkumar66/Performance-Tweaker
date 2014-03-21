package com.rattlehead.cpufrequtils.app;

import java.util.ArrayList;

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
import com.rattlehead.cpufrequtils.app.dialogs.RootAlertDialog;
import com.rattlehead.cpufrequtils.app.utils.Constants;
import com.rattlehead.cpufrequtils.app.utils.CpuUtils;
import com.rattlehead.cpufrequtils.app.utils.RootUtils;

public class CpuFrequencyFragment extends SherlockFragment {
	ArrayWheelAdapter<String> minAdapter;
	ArrayWheelAdapter<String> governorAdapter;

	ArrayList<String> availableFrequencies;
	ArrayList<String> availableGovernors;

	String[] availablefreq;
	String[] availableScalingGovernors;
	String max, min, current_governor;

	View mView;

	AbstractWheel maxFreq, minimum, governor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		availablefreq = CpuUtils.getAvailableFrequencies();
		availableFrequencies = CpuUtils.toArrayList(availablefreq);

		availableScalingGovernors = CpuUtils.getAvailableGovernors();
		availableGovernors = CpuUtils.toArrayList(availableScalingGovernors);

		updateValues();

	}

	private void updateValues() {
		max = CpuUtils.getCurrentMaxFrequeny();

		min = CpuUtils.getCurrentMinFrequency();

		current_governor = CpuUtils.getCurrentScalingGovernor();
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
		maxFreq.setCurrentItem(availableFrequencies.indexOf(max));

		minimum.setViewAdapter(minAdapter);
		minimum.setCurrentItem(availableFrequencies.indexOf(min));

		ArrayWheelAdapter<String> governorAdapter = new ArrayWheelAdapter<String>(
				context, availableScalingGovernors);
		governorAdapter.setItemResource(R.layout.spinner_wheel_box_layout);
		governorAdapter.setItemTextResource(R.id.text);
		governor.setViewAdapter(governorAdapter);
		governor.setCurrentItem(availableGovernors.indexOf(current_governor));

		applyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (RootUtils.isRooted()) {
					CpuUtils.setFrequencyAndGovernor(
							availableFrequencies.get(maxFreq.getCurrentItem()),
							availableFrequencies.get(minimum.getCurrentItem()),
							availableGovernors.get(governor.getCurrentItem()),
							"", mView.getContext());
					updateValues();

				} else {
					new RootAlertDialog().show(getFragmentManager(),
							Constants.tag);
				}
			}
		});
		return mView;
	}
}
