package com.rattlehead.cpufrequtils.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.rattlehead.cpufrequtils.app.utils.CpuUtils;

public class DiskFragment extends SherlockFragment {
	View mView;
	Context mContext;
	AbstractWheel diskScheduler;
	String[] availableSchedulers;
	ArrayWheelAdapter<String> schedulerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		availableSchedulers = CpuUtils.getAvailableIOScheduler();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.disk_control_fragment, container,
				false);
		mContext = mView.getContext();
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		diskScheduler = (AbstractWheel) mView.findViewById(R.id.ioscheduler);
		schedulerAdapter = new ArrayWheelAdapter<String>(mContext,
				availableSchedulers);
		schedulerAdapter.setItemResource(R.layout.spinner_wheel_box_layout);
		schedulerAdapter.setItemTextResource(R.id.text);
		diskScheduler.setViewAdapter(schedulerAdapter);

	}
}
