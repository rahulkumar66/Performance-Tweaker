package com.rattlehead666.performancetweaker.app.ui.fragments;

import com.rattlehead666.performancetweaker.app.R;
import com.rattlehead666.performancetweaker.app.ui.adapters.AlarmTriggerAdapter;
import com.rattlehead666.performancetweaker.app.ui.adapters.CpuWakelocksAdapter;
import com.rattlehead666.performancetweaker.app.ui.adapters.KernelWakelockAdapter;
import com.rattlehead666.performancetweaker.app.utils.SystemAppManagementException;
import com.rattlehead666.performancetweaker.app.utils.SystemAppUtilities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class WakeLocksFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    ListView wakelockList;
    ActionBar actionBar;
    View view;
    Context context;
    TextView timeSince;
    ProgressBar progressBar;
    Spinner spinner;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wakelocksfragment, container, false);
        context = getActivity().getBaseContext();
        wakelockList = (ListView) view.findViewById(R.id.wakelock_data_listview1);
        timeSince = (TextView) view.findViewById(R.id.stats_since);
        spinner = (Spinner) getActivity().findViewById(R.id.spinner_nav);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.loading_main);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        spinner.setVisibility(View.GONE);
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        wakelockList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        adapter = new ArrayAdapter<>(actionBar.getThemedContext(),
                android.R.layout.simple_spinner_dropdown_item,
                context.getResources().getStringArray(R.array.wakelock_actionbar_spinner_items));
        spinner.setVisibility(View.VISIBLE);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        BaseAdapter wakelockAdapter = null;

        switch (position) {
            case 0:
                wakelockAdapter = new KernelWakelockAdapter(context);
                break;
            case 1:
                if (!(SystemAppUtilities.hasBatteryStatsPermission(getActivity()))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(
                            "Since Kitkat google only allows system apps to access battery permissions! Install this app as System app")
                            .setTitle("Install as System app")
                            .setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        SystemAppUtilities.installAsSystemApp(getActivity());
                                    } catch (SystemAppManagementException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }

                wakelockAdapter = new CpuWakelocksAdapter(context);
                break;
            case 2:
                wakelockAdapter = new AlarmTriggerAdapter(context);
                break;
        }
        progressBar.setVisibility(View.GONE);

        if (wakelockAdapter != null && wakelockAdapter.getCount() != 0) {
            wakelockList.setVisibility(View.VISIBLE);
            timeSince.setTextSize(15);
            wakelockList.setAdapter(wakelockAdapter);
            timeSince.setText("");
        } else {
            wakelockList.setVisibility(View.GONE);
            timeSince.setTextSize(20);
            timeSince.setGravity(Gravity.CENTER);
            timeSince.setText(getString(R.string.stats_not_available));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
