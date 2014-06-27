package com.phantomLord.cpufrequtils.app.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asksven.android.common.privateapiproxies.BatteryStatsProxy;
import com.asksven.android.common.privateapiproxies.BatteryStatsTypes;
import com.asksven.android.common.privateapiproxies.StatElement;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.MiscUtils;

public class PartialWakelocksAdapter extends BaseAdapter {
	ArrayList<StatElement> partialWakelocks;
	Context context;
	BatteryStatsProxy stats;

	public PartialWakelocksAdapter(Context ctx) {
		this.context = ctx;

		stats = BatteryStatsProxy.getInstance(context);
		try {
			partialWakelocks = stats.getWakelockStats(context,
					BatteryStatsTypes.WAKE_TYPE_PARTIAL,
					BatteryStatsTypes.STATS_SINCE_UNPLUGGED, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		partialWakelocks = MiscUtils.removeZeroValues(partialWakelocks);
		Collections.sort(partialWakelocks, new Comparator<StatElement>() {
			@Override
			public int compare(StatElement arg0, StatElement arg1) {
				double[] a = arg0.getValues();
				double[] b = arg1.getValues();
				return (int) (b[0] - a[0]);
			}
		});
	}

	@Override
	public int getCount() {
		return partialWakelocks.size();
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
		wakelockName.setText(partialWakelocks.get(position).getName());
		wakeupCount.setText(partialWakelocks.get(position).getData());
		return row;
	}

}
