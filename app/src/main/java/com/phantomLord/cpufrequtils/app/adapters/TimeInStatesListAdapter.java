package com.phantomLord.cpufrequtils.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.Constants;
import com.phantomLord.cpufrequtils.app.utils.CpuState;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;
import com.phantomLord.cpufrequtils.app.utils.TimeInStateReader;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class TimeInStatesListAdapter extends BaseAdapter {

    public long totaltime = 0;
    Context context;
    ArrayList<CpuState> states = new ArrayList<>();
    HashMap<Integer, Long> _states = new HashMap<>();
    TimeInStateReader statesReader;
    LayoutInflater infalter;
    SharedPreferences prefs;
    boolean filterNonZeroVals;

    public TimeInStatesListAdapter(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        filterNonZeroVals = prefs.getBoolean(Constants.PREF_ZERO_VALS, true);
        statesReader = TimeInStateReader.TimeInStatesReader();
        states = statesReader.getCpuStateTime(true, filterNonZeroVals);
        totaltime = statesReader.getTotalTimeInState();
        /*
         * remove zero values
		 */
        infalter = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = infalter.inflate(R.layout.time_in_stat_list_item,
                parent, false);
        TextView frequencyTextView = (TextView) rowView
                .findViewById(R.id.frequency);
        ProgressBar mProgressBar = (ProgressBar) rowView
                .findViewById(R.id.progress);
        TextView time = (TextView) rowView.findViewById(R.id.time);
        TextView percentage = (TextView) rowView.findViewById(R.id.percentage);
        if (states.get(position).getFrequency() == 0)
            frequencyTextView.setText(context.getString(R.string.deep_sleep));
        else
            frequencyTextView
                    .setText((states.get(position).getFrequency() / 1000)
                            + " Mhz");
        time.setText(SysUtils.secToString(states.get(position).getTime() / 100));

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
        if (states != null)
            return states.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return states.get(position);
    }

    @Override
    public long getItemId(int position) {
        return states.indexOf(position);
    }

    public void refresh() {
        states = statesReader.getCpuStateTime(true, filterNonZeroVals);
        totaltime = statesReader.getTotalTimeInState();
        notifyDataSetChanged();
    }

    public void saveOffsets() {
        String data = "";
        ArrayList<CpuState> newStates = statesReader.getCpuStateTime(true,
                filterNonZeroVals);
        for (CpuState state : newStates) {
            data += state.getFrequency() + " " + state.getTime() + ",";
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREF_TIS_RESET_STATS, data);
        editor.apply();
    }

    public void reset() {
        removeOffsets();
        saveOffsets();
        loadPreviousStats();
        states = statesReader.getCpuStateTime(true, filterNonZeroVals);
        totaltime = statesReader.getTotalTimeInState();
        refresh();
    }

    public void loadPreviousStats() {
        String data = prefs.getString(Constants.PREF_TIS_RESET_STATS, null);

        if (data.length() > 0) {
            String[] line = data.split(",");
            for (String str : line) {
                String[] val = str.split(" ");
                _states.put(Integer.parseInt(val[0]), Long.parseLong(val[1]));
            }
        }
        statesReader.newStates = _states;
    }

    public void removeOffsets() {
        if (prefs.getString(Constants.PREF_TIS_RESET_STATS, null) != null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constants.PREF_TIS_RESET_STATS, null);
            editor.apply();
        }
        statesReader.clearNewStates();
    }
}
