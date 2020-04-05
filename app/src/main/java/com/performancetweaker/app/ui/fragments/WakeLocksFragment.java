//package com.performancetweaker.app.ui.fragments;
//
//import android.app.AlertDialog;
//import android.app.Fragment;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.asksven.android.common.utils.DateUtils;
//import com.asksven.android.common.utils.SysUtils;
//import com.performancetweaker.app.R;
//import com.performancetweaker.app.ui.adapters.AlarmTriggerAdapter;
//import com.performancetweaker.app.ui.adapters.CpuWakelocksAdapter;
//import com.performancetweaker.app.ui.adapters.KernelWakelockAdapter;
//import com.performancetweaker.app.utils.Constants;
//import com.performancetweaker.app.utils.SystemAppUtilities;
//
//public class WakeLocksFragment extends Fragment implements AdapterView.OnItemSelectedListener {
//
//    ListView wakelockList;
//    ActionBar actionBar;
//    View view;
//    Context context;
//    TextView timeSince;
//    ProgressBar progressBar;
//    Spinner spinner;
//    ArrayAdapter<String> adapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.wakelocksfragment, container, false);
//        context = getActivity().getBaseContext();
//        wakelockList = view.findViewById(R.id.wakelock_data_listview1);
//        timeSince = view.findViewById(R.id.stats_since);
//        spinner = getActivity().findViewById(R.id.spinner_nav);
//        progressBar = getActivity().findViewById(R.id.loading_main);
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//
//        spinner.setVisibility(View.GONE);
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(true);
//        }
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//
//        wakelockList.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
//        adapter = new ArrayAdapter<>(actionBar.getThemedContext(),
//                android.R.layout.simple_spinner_dropdown_item,
//                context.getResources().getStringArray(R.array.wakelock_actionbar_spinner_items));
//        spinner.setVisibility(View.VISIBLE);
//
//        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        spinner.setOnItemSelectedListener(this);
//        context = getActivity().getBaseContext();
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        BaseAdapter wakelockAdapter = null;
//        wakelockList.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//
//        switch (position) {
//            case 0:
//                wakelockAdapter = new KernelWakelockAdapter(context);
//                break;
//            case 1:
//                boolean hasPermission = SysUtils.hasBatteryStatsPermission(getActivity());
//                Log.d(Constants.App_Tag, "Has Battery Stats Permission " +
//                        hasPermission);
//                if (!hasPermission && !getActivity().isFinishing()) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setMessage(
//                            "Since Kitkat google only allows system apps to access battery permissions! Install this app as System app")
//                            .setTitle("Install as System app")
//                            .setNeutralButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    SystemAppUtilities.installAsSystemApp(getActivity());
//                                }
//                            }).setNegativeButton("No", null).show();
//                } else {
//                    wakelockAdapter = new CpuWakelocksAdapter(context);
//                }
//                break;
//            case 2:
//                wakelockAdapter = new AlarmTriggerAdapter(context);
//                break;
//        }
//       progressBar.setVisibility(View.GONE);
//
//        if (wakelockAdapter != null && wakelockAdapter.getCount() != 0) {
//            wakelockList.setVisibility(View.VISIBLE);
//            timeSince.setTextSize(16);
//            long sinceMs = SystemClock.elapsedRealtime();
//            if (sinceMs != -1) {
//                String sinceText = DateUtils.formatDuration(sinceMs);
//                timeSince.setText(sinceText);
//            } else {
//                timeSince.setText("n/a");
//            }
//            wakelockList.setAdapter(wakelockAdapter);
//        } else {
//            wakelockList.setVisibility(View.GONE);
//            timeSince.setTextSize(20);
//            timeSince.setGravity(Gravity.CENTER);
//            timeSince.setText(getString(R.string.stats_not_available));
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//    }
//}
