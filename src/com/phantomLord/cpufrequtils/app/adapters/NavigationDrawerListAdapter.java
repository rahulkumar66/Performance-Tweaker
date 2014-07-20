package com.phantomLord.cpufrequtils.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.Constants;

public class NavigationDrawerListAdapter extends BaseAdapter {
	private String[] fragmentItems;
	Context context;
	LayoutInflater inflater;
	ImageView imageIcon;
	TextView fragmentName;

	public NavigationDrawerListAdapter(Context ctx) {
		this.context = ctx;
		fragmentItems = Constants.mFragmentsArray;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return fragmentItems.length;
	}

	@Override
	public Object getItem(int position) {
		return fragmentItems[position];
	}

	@Override
	public long getItemId(int position) {
		return fragmentItems[position].indexOf(position);
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup container) {
		View row = inflater
				.inflate(R.layout.drawer_list_item, container, false);
		fragmentName = (TextView) row.findViewById(R.id.fragmentName);
		imageIcon = (ImageView) row.findViewById(R.id.fragmentImage);
		fragmentName.setText(fragmentItems[pos]);
		imageIcon.setImageResource(Constants.icons[pos]);
		return row;
	}
}
