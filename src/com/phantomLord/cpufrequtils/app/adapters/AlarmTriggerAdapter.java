package com.phantomLord.cpufrequtils.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asksven.android.common.nameutils.UidNameResolver;
import com.asksven.android.common.privateapiproxies.Alarm;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.BatteryStatsUtils;

public class AlarmTriggerAdapter extends BaseAdapter {
	ArrayList<Alarm> alarms;
	Context context;
	int totaltime;
	LayoutInflater infalter;

	public AlarmTriggerAdapter(Context ctx) {
		this.context = ctx;
		alarms = BatteryStatsUtils.getAlarmStats(context);
		totaltime = 0;
		for (Alarm e : alarms) {
			totaltime += e.getWakeups();
		}
		infalter = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {

		View row = infalter.inflate(R.layout.alarm_trigger_custom_list_item1,
				container, false);
		TextView AlarmPackageName = (TextView) row
				.findViewById(R.id.alarm_package_name);
		TextView WakeupCount = (TextView) row.findViewById(R.id.wakeup_count);
		TextView name = (TextView) row.findViewById(R.id.alarm_appname);
		ImageView icon = (ImageView) row.findViewById(R.id.alarm_icon);
		ProgressBar progress = (ProgressBar) row
				.findViewById(R.id.alrm_progress);
		Alarm alarm = alarms.get(position);

		icon.setImageDrawable(alarm.getIcon(context));
		String packageName = alarm.getPackageName();
		AlarmPackageName.setText(new UidNameResolver().getLabel(context,
				packageName));
		name.setText(packageName);
		WakeupCount.setText("Wakeups : " + alarm.getWakeups());
		progress.setMax(totaltime);
		progress.setProgress((int) alarm.getWakeups());
		return row;
	}

	@Override
	public int getCount() {
		if (alarms != null)
			return alarms.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return alarms.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return alarms.indexOf(arg0);
	}

}
