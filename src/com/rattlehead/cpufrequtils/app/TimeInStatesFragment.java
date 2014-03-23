package com.rattlehead.cpufrequtils.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.rattlehead.cpufrequtils.app.adapters.TimeInStatesListAdapter;
import com.rattlehead.cpufrequtils.app.utils.CpuState;
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
		setHasOptionsMenu(true);
		view = inflater.inflate(R.layout.time_in_states, container, false);
		listView = (ListView) view.findViewById(R.id.listview1);
		adapter = new TimeInStatesListAdapter(view.getContext());
		listView.setAdapter(adapter);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.add("Show Pie Graph").setIcon(R.drawable.ic_menu_reset)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(getSherlockActivity(),
					TimeInStatePieGraph.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
