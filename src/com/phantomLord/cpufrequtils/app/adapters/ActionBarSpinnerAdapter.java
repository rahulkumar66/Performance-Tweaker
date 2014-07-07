package com.phantomLord.cpufrequtils.app.adapters;

import com.phantomLord.cpufrequtils.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ActionBarSpinnerAdapter extends BaseAdapter {
	private String[] itemNames;
	Context context;
	LayoutInflater inflator;

	public ActionBarSpinnerAdapter(Context ctx) {
		this.context = ctx;
		itemNames = ctx.getResources().getStringArray(
				R.array.wakelock_actionbar_spinner_items);
		inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return itemNames.length;
	}

	@Override
	public Object getItem(int index) {
		return itemNames[index];
	}

	@Override
	public long getItemId(int index) {
		return itemNames[index].indexOf(index);
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup container) {
	//View row=inflator.inflate(R.layout., root);
		return null;
	}

}
