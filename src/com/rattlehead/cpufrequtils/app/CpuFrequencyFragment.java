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
import com.rattlehead.cpufrequtils.app.utils.CpuUtils;

public class CpuFrequencyFragment extends SherlockFragment {
	ArrayWheelAdapter<String> minAdapter;
	ArrayWheelAdapter<String> governorAdapter;
	View mView;
	ArrayList<String> availableFrequencies;
	ArrayList<String> availableGovernors;
	String[] availablefreq;
	String[] availableScalingGovernors;
	String max, min, current_governor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		availableFrequencies = CpuUtils.toArrayList(CpuUtils
				.getAvailableFrequencies());
		availableGovernors = CpuUtils.toArrayList(CpuUtils
				.getAvailableGovernors());
		availablefreq = CpuUtils.getAvailableFrequencies();
		availableScalingGovernors = CpuUtils.getAvailableGovernors();
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
		Context context = mView.getContext();
		final AbstractWheel maxFreq, minimum, governor;
		maxFreq = (AbstractWheel) mView.findViewById(R.id.mins);
		minimum = (AbstractWheel) mView.findViewById(R.id.minimumfreq_spinner);
		governor = (AbstractWheel) mView.findViewById(R.id.governor_spinner);
		Button applyButton = (Button) mView.findViewById(R.id.button_apply);

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
				new Thread(new Runnable() {

					@Override
					public void run() {
						CpuUtils.setFrequencyAndGovernor(availableFrequencies
								.get(maxFreq.getCurrentItem()),
								availableFrequencies.get(minimum
										.getCurrentItem()), availableGovernors
										.get(governor.getCurrentItem()), "sio",
								mView.getContext());
						updateValues();
					}
				}).start();
			}
		});
		return mView;
	}

}
