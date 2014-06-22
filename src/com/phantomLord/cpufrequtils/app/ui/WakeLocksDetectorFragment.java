package com.phantomLord.cpufrequtils.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.adapters.AlarmTriggerAdapter;

public class WakeLocksDetectorFragment extends SherlockFragment implements
		OnNavigationListener {
	ListView alarmTriggers;
	ActionBar actionBar;
	View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Kernel Wakelocks
		 * 
		 * ArrayList<StatElement> kernelWakelock = Wakelocks
		 * .parseProcWakelocks(getSherlockActivity()); for (StatElement
		 * statElement : kernelWakelock) { Log.d("chemical warfare",
		 * statElement.toString()); }
		 */
		/*
		 * Partial Wakelocks
		 */
		/*
		 * ArrayList<StatElement>
		 * pwl=PartialWakelocksDumpsys.getPartialWakelocks
		 * (getSherlockActivity()); for (StatElement statElement : pwl) {
		 * Log.d("damage case",statElement.toString()); }
		 */
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
		actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		alarmTriggers
				.setAdapter(new AlarmTriggerAdapter(getSherlockActivity()));

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
			Toast.makeText(getSherlockActivity(), "tin tin tan",
					Toast.LENGTH_SHORT).show();
			break;
		case 1:
			break;
		case 2:
			break;
		}
		return true;
	}

}
