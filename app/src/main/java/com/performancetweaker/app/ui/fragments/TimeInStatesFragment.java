package com.performancetweaker.performancetweaker.app.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.performancetweaker.performancetweaker.app.R;
import com.performancetweaker.performancetweaker.app.ui.adapters.TimeInStatesListAdapter;
import com.performancetweaker.performancetweaker.app.utils.Constants;
import com.performancetweaker.performancetweaker.app.utils.SysUtils;

public class TimeInStatesFragment extends Fragment {

    View view;
    ListView listView;
    TimeInStatesListAdapter timeInStateAdapter;
    TextView totalTimeInState;
    SharedPreferences prefs;
    Context context;
    CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.time_in_states, container, false);
        listView = (ListView) view.findViewById(R.id.time_in_state_listView);
        totalTimeInState = (TextView) view.findViewById(R.id.total_time);
        cardView = (CardView) view.findViewById(R.id.total_time_card);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = view.getContext();
        timeInStateAdapter = new TimeInStatesListAdapter(context);

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String previousStats = prefs.getString(Constants.PREF_TIS_RESET_STATS, null);

        if (previousStats != null) {
            timeInStateAdapter.loadPreviousStats();
        }

        ViewGroup header = (ViewGroup) getActivity().getLayoutInflater()
                .inflate(R.layout.time_in_state_list_header, listView, false);
        listView.addHeaderView(header, null, false);
        listView.setAdapter(timeInStateAdapter);

        totalTimeInState.setText(SysUtils.secToString(timeInStateAdapter.totaltime / 100));
        timeInStateAdapter.refresh();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.time_in_stat_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                timeInStateAdapter.refresh();
                totalTimeInState.setText(SysUtils.secToString(timeInStateAdapter.totaltime / 100));
                break;

            case R.id.reset_timers:
                timeInStateAdapter.reset();
                totalTimeInState.setText(SysUtils.secToString(timeInStateAdapter.totaltime / 100));
                break;

            case R.id.restore_timers:
                timeInStateAdapter.removeOffsets();
                timeInStateAdapter.refresh();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
