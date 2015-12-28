package com.rattlehead666.performancetweaker.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.asksven.android.common.privateapiproxies.NativeKernelWakelock;
import com.rattlehead666.performancetweaker.app.R;
import com.rattlehead666.performancetweaker.app.utils.BatteryStatsUtils;
import com.rattlehead666.performancetweaker.app.utils.SysUtils;
import java.util.ArrayList;

public class KernelWakelockAdapter extends BaseAdapter {

  ArrayList<NativeKernelWakelock> kernelWakelocks;
  Context context;
  LayoutInflater inflater;
  int totaltime;

  public KernelWakelockAdapter(Context ctx) {
    this.context = ctx;
    kernelWakelocks = BatteryStatsUtils.getInstance(context).getNativeKernelWakelocks(true);
    totaltime = 0;
    for (NativeKernelWakelock element : kernelWakelocks) {
      totaltime += (int) element.getDuration() / 1000;
    }
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override public View getView(int position, View arg1, ViewGroup arg2) {
    View rowView = inflater.inflate(R.layout.kernel_wakelock_row, arg2, false);
    TextView mKernelWakelock = (TextView) rowView.findViewById(R.id.kernel_wakelock_name);
    TextView WakeupInfo = (TextView) rowView.findViewById(R.id.wakelock_duration);
    TextView wakeUpCount = (TextView) rowView.findViewById(R.id.kernel_wakelock_count);
    ProgressBar progress = (ProgressBar) rowView.findViewById(R.id.kernel_progress);

    NativeKernelWakelock nativeWakeLock = kernelWakelocks.get(position);
    String kernelWakelock = kernelWakelocks.get(position).getName();
    mKernelWakelock.setText(kernelWakelock);
    WakeupInfo.setText(SysUtils.secToString(nativeWakeLock.getDuration() / 1000));
    progress.setMax(totaltime);
    progress.setProgress((int) nativeWakeLock.getDuration() / 1000);
    wakeUpCount.setText("x" + nativeWakeLock.getCount() + " " + context.getString(R.string.times));

    return rowView;
  }

  @Override public int getCount() {
    if (kernelWakelocks != null) {
      return kernelWakelocks.size();
    } else {
      return 0;
    }
  }

  @Override public Object getItem(int pos) {
    return kernelWakelocks.get(pos);
  }

  @Override public long getItemId(int pos) {
    return kernelWakelocks.indexOf(pos);
  }
}
