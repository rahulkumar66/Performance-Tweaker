package com.rattlehead.cpufrequtils.app;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.rattlehead.cpufrequtils.app.adapters.TimeInStatesListAdapter;
import com.rattlehead.cpufrequtils.app.dialogs.RootAlertDialog;
import com.rattlehead.cpufrequtils.app.utils.Constants;
import com.rattlehead.cpufrequtils.app.utils.CpuState;
import com.rattlehead.cpufrequtils.app.utils.CpuUtils;
import com.rattlehead.cpufrequtils.app.utils.RootUtils;
import com.rattlehead.cpufrequtils.app.utils.TimeInStateReader;

public class TimeInStatesFragment extends SherlockFragment {

	View view;
	ListView listView;
	ArrayList<CpuState> states;
	TimeInStatesListAdapter adapter;
	TimeInStateReader statesMonitor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.time_in_states, container, false);
		listView = (ListView) view.findViewById(R.id.listview1);
		adapter = new TimeInStatesListAdapter(view.getContext());
		listView.setAdapter(adapter);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

}
