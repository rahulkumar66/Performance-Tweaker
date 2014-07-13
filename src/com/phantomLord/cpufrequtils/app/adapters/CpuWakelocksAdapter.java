package com.phantomLord.cpufrequtils.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asksven.android.common.privateapiproxies.Wakelock;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.BatteryStatsUtils;
import com.phantomLord.cpufrequtils.app.utils.MiscUtils;

public class CpuWakelocksAdapter extends BaseAdapter {
	ArrayList<Wakelock> partialWakelocks;
	Context context;

	public CpuWakelocksAdapter(Context ctx) {
		this.context = ctx;
		partialWakelocks = BatteryStatsUtils
				.getCpuWakelocksStats(context, true);
	}

	@Override
	public int getCount() {
		if (partialWakelocks != null)
			return partialWakelocks.size();
		else
			return 0;

	}

	@Override
	public Object getItem(int arg0) {
		return partialWakelocks.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return partialWakelocks.indexOf(arg0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater infalter = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = infalter.inflate(R.layout.alarm_trigger_custom_list_item1,
				parent, false);
		TextView wakelockName = (TextView) row
				.findViewById(R.id.alarm_package_name);
		TextView wakeupCount = (TextView) row.findViewById(R.id.wakeup_count);
		Wakelock mWakelock = partialWakelocks.get(position);

		wakelockName.setText(mWakelock.getName());
		wakeupCount.setText("Duration : "
				+ MiscUtils.secToString(mWakelock.getDuration() / 1000));
		return row;
	}

}
