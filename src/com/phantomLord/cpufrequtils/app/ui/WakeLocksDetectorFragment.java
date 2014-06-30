package com.phantomLord.cpufrequtils.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.adapters.AlarmTriggerAdapter;
import com.phantomLord.cpufrequtils.app.adapters.KernelWakelockAdapter;
import com.phantomLord.cpufrequtils.app.adapters.PartialWakelocksAdapter;

public class WakeLocksDetectorFragment extends SherlockFragment implements
		OnNavigationListener {

	ListView alarmTriggers;
	ActionBar actionBar;
	View view;
	Context context;

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
		alarmTriggers = (ListView) view
				.findViewById(R.id.alarmtriggerlistview1);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = view.getContext();
		alarmTriggers.setAdapter(new AlarmTriggerAdapter(context));

		actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<CharSequence> mSpinnerAdapter = ArrayAdapter
				.createFromResource(getSherlockActivity(),
						R.array.spinneritems,
						android.R.layout.simple_dropdown_item_1line);
		actionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
	}

	@Override
	public void onPause() {
		super.onPause();
		actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {

		switch (itemPosition) {
		case 0:
			alarmTriggers.setAdapter(new AlarmTriggerAdapter(context));
			break;
		case 1:
			alarmTriggers.setAdapter(new PartialWakelocksAdapter(context));
			break;
		case 2:
			alarmTriggers.setAdapter(new KernelWakelockAdapter(context));
			break;
		}
		return true;
	}

}
