package com.rattlehead.cpufrequtils.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rattlehead.cpufrequtils.app.R;
import com.rattlehead.cpufrequtils.app.utils.Constants;
import com.rattlehead.cpufrequtils.app.utils.CpuState;
import com.rattlehead.cpufrequtils.app.utils.CpuUtils;

public class CustomListAdapter extends BaseAdapter {
	Context context;
	ArrayList<CpuState> states;
	long totaltime = 0;

	public CustomListAdapter(Context context, ArrayList<CpuState> values) {
		this.context = context;
		this.states = values;
		totaltime = CpuUtils.getTotalTimeInState();
		Log.d(Constants.tag, "total time= " + totaltime);
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
		TextView time=(TextView) rowView.findViewById(R.id.time);
		
		time.setText(String.valueOf(states.get(position).getTime()));
		if (states.get(position).getFrequency() == 0) 
			frequencyTextView.setText("Deep Sleep");
		 else
			frequencyTextView.setText(String.valueOf(states.get(position)
					.getFrequency()));
		
		mProgressBar.setMax((int) (totaltime));
		// Log.d(Constants.tag,"inside adapter "+CpuUtils.getTotalTimeInState());
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

}
