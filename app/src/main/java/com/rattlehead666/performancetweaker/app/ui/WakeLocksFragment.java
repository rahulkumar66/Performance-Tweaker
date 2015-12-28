package com.rattlehead666.performancetweaker.app.ui;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import app.phantomLord.cpufrequtils.app.R;
import com.rattlehead666.performancetweaker.app.ui.adapters.AlarmTriggerAdapter;
import com.rattlehead666.performancetweaker.app.ui.adapters.CpuWakelocksAdapter;
import com.rattlehead666.performancetweaker.app.ui.adapters.KernelWakelockAdapter;
import com.rattlehead666.performancetweaker.app.utils.SystemAppManagementException;
import com.rattlehead666.performancetweaker.app.utils.SystemAppUtilities;

public class WakeLocksFragment extends Fragment implements ActionBar.OnNavigationListener {

  ListView wakelockList;
  ActionBar actionBar;
  View view;
  Context context;
  TextView timeSince;
  BaseAdapter adapter;
  ProgressBar progressBar;

  @Override public void onResume() {
    super.onResume();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.wakelocksfragment, container, false);

    context = getActivity().getBaseContext();
    wakelockList = (ListView) view.findViewById(R.id.wakelock_data_listview1);
    timeSince = (TextView) view.findViewById(R.id.stats_since);

    return view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

    progressBar = (ProgressBar) getActivity().findViewById(R.id.loading_main);

    wakelockList.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);

    actionBar.setTitle("");
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

    adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
        context.getResources().getStringArray(R.array.wakelock_actionbar_spinner_items));
    actionBar.setListNavigationCallbacks(adapter, this);
    actionBar.setSelectedNavigationItem(0);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
  }

  @Override public boolean onNavigationItemSelected(int itemPosition, long itemId) {
    BaseAdapter wakelockAdapter = null;

    progressBar.setVisibility(View.GONE);

    switch (itemPosition) {
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
                @Override public void onClick(DialogInterface dialog, int which) {
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

    return true;
  }
}
