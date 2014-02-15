package com.rattlehead.cpufrequtils.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.rattlehead.cpufrequtils.app.utils.CpuUtils;

public class TimeInStatesFragment extends SherlockFragment {
	
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.time_in_states, container,false);
		CpuUtils.getCpuStateTime();
		return view;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

}
