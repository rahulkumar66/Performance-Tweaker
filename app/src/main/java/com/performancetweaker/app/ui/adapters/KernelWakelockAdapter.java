//package com.performancetweaker.app.ui.adapters;
//
//import android.content.Context;
//import android.os.SystemClock;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.asksven.android.common.privateapiproxies.NativeKernelWakelock;
//import com.github.lzyzsd.circleprogress.DonutProgress;
//import com.performancetweaker.app.R;
//import com.performancetweaker.app.utils.BatteryStatsUtils;
//import com.performancetweaker.app.utils.SysUtils;
//
//import java.util.ArrayList;
//
//public class KernelWakelockAdapter extends BaseAdapter {
//
//    private ArrayList<NativeKernelWakelock> kernelWakelocks;
//    private Context context;
//    private LayoutInflater inflater;
//    private long totalTime = 0;
//
//    public KernelWakelockAdapter(Context ctx) {
//        this.context = ctx;
//        kernelWakelocks = BatteryStatsUtils.getInstance(context).getNativeKernelWakelocks(true);
//        totalTime = SystemClock.elapsedRealtime();
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View rowView = inflater.inflate(R.layout.kernel_wakelock_row, parent, false);
//        TextView mKernelWakelock = rowView.findViewById(R.id.kernel_wakelock_name);
//        TextView WakeupInfo = rowView.findViewById(R.id.wakelock_duration);
//        TextView wakeUpCount = rowView.findViewById(R.id.kernel_wakelock_count);
//        DonutProgress progress = rowView.findViewById(R.id.donut_progress);
//
//        NativeKernelWakelock nativeWakeLock = kernelWakelocks.get(position);
//        if(nativeWakeLock!=null) {
//            String kernelWakelock = kernelWakelocks.get(position).getName();
//            mKernelWakelock.setText(kernelWakelock);
//            WakeupInfo.setText(SysUtils.secToString(nativeWakeLock.getDuration() / 1000));
//            progress.setMax(100);
//            progress.setProgress(nativeWakeLock.getDuration() * 100 / totalTime);
//            wakeUpCount.setText("x" + nativeWakeLock.getCount() + " " + context.getString(R.string.times));
//        }
//        return rowView;
//    }
//
//    @Override
//    public int getCount() {
//        if (kernelWakelocks != null) {
//            return kernelWakelocks.size();
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public Object getItem(int pos) {
//        return kernelWakelocks.get(pos);
//    }
//
//    @Override
//    public long getItemId(int pos) {
//        return kernelWakelocks.indexOf(pos);
//    }
//}
