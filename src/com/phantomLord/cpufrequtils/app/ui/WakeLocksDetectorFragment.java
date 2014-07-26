package com.phantomLord.cpufrequtils.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.adapters.AlarmTriggerAdapter;
import com.phantomLord.cpufrequtils.app.adapters.CpuWakelocksAdapter;
import com.phantomLord.cpufrequtils.app.adapters.KernelWakelockAdapter;
import com.phantomLord.cpufrequtils.app.adapters.WakelockActionBarSpinnerAdapter;
import com.phantomLord.cpufrequtils.app.utils.BatteryStatsUtils;

public class WakeLocksDetectorFragment extends SherlockFragment implements
		OnNavigationListener {

	ListView wakelockList;
	ActionBar actionBar;
	View view;
	Context themedContext, context;
	TextView timeSince;
	BaseAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.wakelocksfragment, container, false);
		actionBar = getSherlockActivity().getSupportActionBar();
		themedContext = actionBar.getThemedContext();
		context = getSherlockActivity().getBaseContext();
		wakelockList = (ListView) view
				.findViewById(R.id.wakelock_data_listview1);
		timeSince = (TextView) view.findViewById(R.id.stats_since);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		adapter = new WakelockActionBarSpinnerAdapter(themedContext);
		actionBar.setListNavigationCallbacks(adapter, this);
		actionBar.setSelectedNavigationItem(0);
	}

	@Override
	public void onDestroyView() {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		super.onDestroyView();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		BaseAdapter adapter = null;
		switch (itemPosition) {
		case 0:
			adapter = new KernelWakelockAdapter(context);
			timeSince.setText(getString(R.string.time_since) + " "
					+ BatteryStatsUtils.getTimeSinceForKernelWakelocks());
			break;
		case 1:
			adapter = new CpuWakelocksAdapter(context);
			timeSince.setText(getString(R.string.time_since) + " "
					+ BatteryStatsUtils.getTimeSinceForCpuWakelocks());
			break;
		case 2:
			adapter = new AlarmTriggerAdapter(context);
			timeSince.setText(getString(R.string.time_since) + " "
					+ BatteryStatsUtils.getTimeSinceForKernelWakelocks());
			break;
		}
		if (adapter.getCount() != 0) {
			wakelockList.setVisibility(View.VISIBLE);
			timeSince.setTextSize(15);
			wakelockList.setAdapter(adapter);
		} else {
			wakelockList.setVisibility(View.GONE);
			timeSince.setTextSize(20);
			timeSince.setGravity(Gravity.CENTER);
			timeSince.setText(getString(R.string.stats_not_available));
		}
		return true;
	}
}
