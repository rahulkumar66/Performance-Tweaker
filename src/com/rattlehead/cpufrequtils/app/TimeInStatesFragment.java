package com.rattlehead.cpufrequtils.app;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.rattlehead.cpufrequtils.app.adapters.CustomListAdapter;
import com.rattlehead.cpufrequtils.app.utils.CpuState;
import com.rattlehead.cpufrequtils.app.utils.CpuUtils;

public class TimeInStatesFragment extends SherlockFragment {

	View view;
	ListView listView;
	ArrayList<CpuState> states;
	CustomListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(getTag(), "oncreateview");
		view = inflater.inflate(R.layout.time_in_states, container, false);
		states=CpuUtils.getCpuStateTime();
		listView=(ListView) view.findViewById(R.id.listview1);

		adapter=new CustomListAdapter(view.getContext(),states);
		listView.setAdapter(adapter);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	

}
