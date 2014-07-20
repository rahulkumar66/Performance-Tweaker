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

	@Override
	public void onResume() {
		super.onResume();
		actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.wakelocksfragment, container, false);

		wakelockList = (ListView) view
				.findViewById(R.id.wakelock_data_listview1);
		timeSince = (TextView) view.findViewById(R.id.stats_since);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		themedContext = getSherlockActivity().getSupportActionBar()
				.getThemedContext();
		context = getSherlockActivity().getBaseContext();

		actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(
				new WakelockActionBarSpinnerAdapter(themedContext), this);
		wakelockList.setAdapter(new KernelWakelockAdapter(context));
		timeSince.setText("Time Since : "
				+ BatteryStatsUtils.getTimeSinceForKernelWakelocks());

	}

	@Override
	public void onPause() {
		super.onPause();
		actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		BaseAdapter adapter = null;

		switch (itemPosition) {
		case 0:
			adapter = new KernelWakelockAdapter(context);
			timeSince.setText("Time Since : "
					+ BatteryStatsUtils.getTimeSinceForKernelWakelocks());
			break;
		case 1:
			adapter = new CpuWakelocksAdapter(context);
			timeSince.setText("Time Since : "
					+ BatteryStatsUtils.getTimeSinceForCpuWakelocks());
			break;
		case 2:
			adapter = new AlarmTriggerAdapter(context);
			timeSince.setText("Time Since : "
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
			timeSince
					.setText("Statistics are not available yet , Please Give it some time");
		}
		return true;
	}
}
