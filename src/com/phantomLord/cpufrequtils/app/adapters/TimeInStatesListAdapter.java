package com.phantomLord.cpufrequtils.app.adapters;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.CpuState;
import com.phantomLord.cpufrequtils.app.utils.MiscUtils;
import com.phantomLord.cpufrequtils.app.utils.TimeInStateReader;

public class TimeInStatesListAdapter extends BaseAdapter {
	Context context;
	ArrayList<CpuState> states;
	public long totaltime = 0;
	TimeInStateReader statesReader;

	public TimeInStatesListAdapter(Context context) {
		statesReader = new TimeInStateReader();
		this.context = context;
		states = statesReader.getCpuStateTime(true);
		totaltime = statesReader.getTotalTimeInState();
		Collections.sort(states);
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
		TextView percentage = (TextView) rowView.findViewById(R.id.percentage);

		time.setText(MiscUtils.secToString(states.get(position).getTime()));
		if (states.get(position).getFrequency() == 0)
			frequencyTextView.setText("Deep Sleep");
		else
			frequencyTextView
					.setText((states.get(position).getFrequency() / 1000)
							+ " Mhz");
		mProgressBar.setMax((int) (totaltime));
		mProgressBar.setProgress((int) (states.get(position).getTime()));

		/*
		 * calculate percentage of time
		 */
		long percent = (states.get(position).getTime() * 100) / totaltime;
		percentage.setText(percent + "%");
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

}
