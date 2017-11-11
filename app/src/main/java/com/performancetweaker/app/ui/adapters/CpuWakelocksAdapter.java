package com.performancetweaker.app.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksven.android.common.nameutils.UidNameResolver;
import com.asksven.android.common.privateapiproxies.Wakelock;
import com.asksven.android.common.utils.DateUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.BatteryStatsUtils;

import java.util.ArrayList;

public class CpuWakelocksAdapter extends BaseAdapter {

    private ArrayList<Wakelock> partialWakelocks;
    private Context context;
    private int totalTime;
    private LayoutInflater inflater;

    public CpuWakelocksAdapter(Context ctx) {
        this.context = ctx;
        partialWakelocks = BatteryStatsUtils.getInstance(context).getCpuWakelocksStats(false);
        totalTime = (int) SystemClock.elapsedRealtime();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (partialWakelocks != null) {
            return partialWakelocks.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int arg0) {
        return partialWakelocks.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return partialWakelocks.indexOf(arg0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.cpu_wakelock_row, parent, false);
        TextView wakelockName = row.findViewById(R.id.cpu_wakelock_name);
        TextView packageNameView = row.findViewById(R.id.cpu_wakelock_duration);
        TextView wakelockDetail = row.findViewById(R.id.cpu_wakelock_count);
        ImageView packageIcon = row.findViewById(R.id.package_icon);
        DonutProgress progress = row.findViewById(R.id.cpu_wakelock_progress);

        Wakelock wakelock = partialWakelocks.get(position);
        String packageName=wakelock.getFqn(UidNameResolver.getInstance());
        Drawable drawable = wakelock.getIcon(UidNameResolver.getInstance());
        if (drawable != null) {
            packageIcon.setImageDrawable(drawable);
        }
        wakelockName.setText(wakelock.getName());
        packageNameView.setText(packageName);
        wakelockDetail.setText(DateUtils.formatDuration(wakelock.getDuration())
                + " Count: " + wakelock.getCount());
        progress.setMax(totalTime);
        progress.setProgress((wakelock.getDuration()*100)/totalTime);
        return row;
    }
}
