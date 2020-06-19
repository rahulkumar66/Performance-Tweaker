package com.performancetweaker.app.ui.adapters;

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

import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.Constants;
import com.performancetweaker.app.utils.CpuState;
import com.performancetweaker.app.utils.SysUtils;
import com.performancetweaker.app.utils.TimeInStateReader;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class TimeInStatesListAdapter extends BaseAdapter {

    public long totaltime;
    Context context;
    ArrayList<CpuState> states;
    HashMap<Integer, Long> _states = new HashMap<>();
    TimeInStateReader statesReader;
    SharedPreferences prefs;
    boolean filterNonZeroVals;

    public TimeInStatesListAdapter(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        filterNonZeroVals = prefs.getBoolean(Constants.PREF_ZERO_VALS, true);
        statesReader = TimeInStateReader.TimeInStatesReader();
        states = statesReader.getCpuStateTime(true, filterNonZeroVals);
        totaltime = statesReader.getTotalTimeInState();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.time_in_stat_list_item, parent, false);
        }
        TextView frequencyTextView = convertView.findViewById(R.id.frequency);
        ProgressBar mProgressBar = convertView.findViewById(R.id.progress);
        TextView time = convertView.findViewById(R.id.time);
        TextView percentage = convertView.findViewById(R.id.percentage);
        CpuState state = states.get(position);

        if (state != null) {
            if (state.getFrequency() == 0) {
                frequencyTextView.setText(context.getString(R.string.deep_sleep));
            } else {
                frequencyTextView.setText((states.get(position).getFrequency() / 1000) + " Mhz");
            }
            time.setText(SysUtils.secToString(states.get(position).getTime() / 100));

            mProgressBar.setMax((int) (totaltime));
            mProgressBar.setProgress((int) (states.get(position).getTime()));

            /*
             * calculate percentage of time
             */
            long percent = (states.get(position).getTime() * 100) / totaltime;
            percentage.setText(percent + "%");
        }

        return convertView;
    }

    @Override
    public int getCount() {
        if (states != null) {
            return states.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return states.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refresh() {
        states = statesReader.getCpuStateTime(true, filterNonZeroVals);
        totaltime = statesReader.getTotalTimeInState();
        notifyDataSetChanged();
    }

    public void saveOffsets() {
        String data = "";
        ArrayList<CpuState> newStates = statesReader.getCpuStateTime(true, filterNonZeroVals);
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
