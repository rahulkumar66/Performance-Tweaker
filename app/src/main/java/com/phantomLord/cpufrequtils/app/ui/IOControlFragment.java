package com.phantomLord.cpufrequtils.app.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.utils.Constants;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IOControlFragment extends Fragment {
    View mView;
    Context mContext;
    //AbstractWheel diskScheduler, readAhead;
    String[] availableSchedulers, readAheadValues;
    //	ArrayWheelAdapter<String> schedulerAdapter, readAheadAdapter;
    String currentScheduler;
    List<String> schedulers = new ArrayList<String>();
    List<String> availableReadAheadValues = new ArrayList<String>();
    Button applyButton;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        availableSchedulers = SysUtils.getAvailableIOScheduler();
        schedulers = Arrays.asList(availableSchedulers);
        currentScheduler = SysUtils.getCurrentIOScheduler();
        readAheadValues = Constants.readAheadKb;
        availableReadAheadValues = Arrays.asList(readAheadValues);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //	mView = inflater.inflate(R.layout.disk_control_fragment, container,
        //			false);
        mContext = mView.getContext();
        applyButton = (Button) mView.findViewById(R.id.disk_apply);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*diskScheduler = (AbstractWheel) mView.findViewById(R.id.ioscheduler);
		readAhead = (AbstractWheel) mView.findViewById(R.id.readahead1);
		schedulerAdapter = new ArrayWheelAdapter<String>(mContext,
				availableSchedulers);
		schedulerAdapter.setItemResource(R.layout.spinner_wheel_box_layout);
		schedulerAdapter.setItemTextResource(R.id.text);
		diskScheduler.setViewAdapter(schedulerAdapter);
		diskScheduler.setCurrentItem(schedulers.indexOf(currentScheduler));

		readAheadAdapter = new ArrayWheelAdapter<String>(mContext,
				readAheadValues);
		readAheadAdapter.setItemResource(R.layout.spinner_wheel_box_layout);
		readAheadAdapter.setItemTextResource(R.id.text);
		readAhead.setViewAdapter(readAheadAdapter);
		readAhead.setCurrentItem(availableReadAheadValues.indexOf(SysUtils
				.getReadAhead()));

		applyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SysUtils.isRooted()) {
					String mScheduler = schedulers.get(diskScheduler
							.getCurrentItem());
					String read_ahead = availableReadAheadValues.get(readAhead
							.getCurrentItem());
					SysUtils.setDiskSchedulerandReadAhead(mScheduler,
							read_ahead, mContext);
					prefs = PreferenceManager
							.getDefaultSharedPreferences(mContext);
					if (prefs
							.getBoolean(Constants.PREF_IO_APPLY_ON_BOOT, false)) {
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString(Constants.PREF_IO_SCHEDULER,
								mScheduler);
						editor.putString(Constants.PREF_READ_AHEAD, read_ahead);
						editor.commit();
					}
				} else {
					new RootNotFoundAlertDialog().show(getFragmentManager(),
							Constants.App_Tag);
				}
			}
		});
        */
    }

}
