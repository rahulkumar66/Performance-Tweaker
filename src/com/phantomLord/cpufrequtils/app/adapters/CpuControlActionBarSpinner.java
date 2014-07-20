package com.phantomLord.cpufrequtils.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.Constants;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;

public class CpuControlActionBarSpinner extends BaseAdapter {
	int noOfCpuCores = 0;
	Context context;
	String[] cores;
	LayoutInflater inflator;

	public CpuControlActionBarSpinner(Context ctx) {
		this.context = ctx;
		noOfCpuCores = SysUtils.getCoreCount();
		if (noOfCpuCores == 1) {
			cores = new String[noOfCpuCores];
		} else {
			cores = new String[noOfCpuCores + 1];
			cores[noOfCpuCores] = Constants.CPU_ALL;
		}
		for (int i = 0; i < noOfCpuCores; i++) {
			cores[i] = "Core " + i;
		}
		inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (noOfCpuCores == 1) {
			return noOfCpuCores;
		} else if (noOfCpuCores != 0) {
			return cores.length;
		} else
			return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return cores[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return cores[arg0].hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		View row = inflator.inflate(R.layout.sherlock_spinner_dropdown_item,
				container, false);
		TextView text = (TextView) row.findViewById(android.R.id.text1);
		text.setText(cores[position]);
		return row;
	}

}
