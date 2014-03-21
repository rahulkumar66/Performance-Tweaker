package com.rattlehead.cpufrequtils.app.adapters;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rattlehead.cpufrequtils.app.R;
import com.rattlehead.cpufrequtils.app.utils.CpuState;
import com.rattlehead.cpufrequtils.app.utils.TimeInStateReader;

public class TimeInStatesListAdapter extends BaseAdapter {
	Context context;
	ArrayList<CpuState> states;
	long totaltime = 0;
	long hours, minute, second;
	TimeInStateReader statesReader;

	public TimeInStatesListAdapter(Context context) {
		statesReader=new TimeInStateReader();
		this.context = context;
		states=statesReader.getCpuStateTime(true);
		totaltime = statesReader.getTotalTimeInState();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater infalter = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = infalter.inflate(R.layout.custom_list_item1, parent,
				false);
		TextView frequencyTextView = (TextView) rowView
				.findViewById(R.id.frequency);
		ProgressBar mProgressBar = (ProgressBar) rowView
				.findViewById(R.id.progress);
		TextView time = (TextView) rowView.findViewById(R.id.time);

		calculateTime(states.get(position).getTime());
		time.setText("" + hours + " h " + minute + " m " + second + " s ");
		if (states.get(position).getFrequency() == 0)
			frequencyTextView.setText("Deep Sleep");
		else
			frequencyTextView.setText((states.get(position)
					.getFrequency()/1000)+" Mhz");
		mProgressBar.setMax((int) (totaltime));
		mProgressBar.setProgress((int) (states.get(position).getTime()));
		return rowView;
	}

	@Override
	public int getCount() {
		return states.size();
	}

	@Override
	public Object getItem(int position) {
		return states.get(position);
	}

	@Override
	public long getItemId(int position) {
		return states.indexOf(position);
	}

	public void calculateTime(long seconds) {
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
		minute = TimeUnit.SECONDS.toMinutes(seconds)
				- (TimeUnit.SECONDS.toHours(seconds) * 60);
		second = TimeUnit.SECONDS.toSeconds(seconds)
				- (TimeUnit.SECONDS.toMinutes(seconds) * 60);

	}

}
