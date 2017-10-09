package com.performancetweaker.app.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asksven.android.common.nameutils.UidNameResolver;
import com.asksven.android.common.privateapiproxies.Wakelock;
import com.asksven.android.common.utils.DateUtils;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.BatteryStatsUtils;
import com.performancetweaker.app.utils.SysUtils;

import java.util.ArrayList;

public class CpuWakelocksAdapter extends BaseAdapter {

    private ArrayList<Wakelock> partialWakelocks;
    private Context context;
    private int totalTime;
    private LayoutInflater inflater;
    private UidNameResolver uidNameResolver;

    public CpuWakelocksAdapter(Context ctx) {
        this.context = ctx;
        partialWakelocks = BatteryStatsUtils.getInstance(context).getCpuWakelocksStats(false);
        /*
         * calculate total time
		 */
        totalTime = (int) SystemClock.elapsedRealtime();
//        if (partialWakelocks != null && partialWakelocks.size() != 0) {
//            totalTime = 0;
//            for (Wakelock wl : partialWakelocks) {
//                totalTime += wl.getDuration() / 1000;
//            }
//        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        uidNameResolver = UidNameResolver.getInstance(context);
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
        TextView duration = row.findViewById(R.id.cpu_wakelock_duration);
        TextView wakeCount = row.findViewById(R.id.cpu_wakelock_count);
        ImageView packageIcon = row.findViewById(R.id.package_icon);
        DonutProgress progress = row.findViewById(R.id.cpu_wakelock_progress);

        Wakelock mWakelock = partialWakelocks.get(position);
        Drawable drawable = mWakelock.getIcon(uidNameResolver);
        Log.d("tag",mWakelock.getPackageName()+"q"+mWakelock.getUidInfo());
        if (drawable != null) {
            packageIcon.setImageDrawable(drawable);
        }
        wakelockName.setText(mWakelock.getName());
        duration.setText(DateUtils.formatDuration(mWakelock.getDuration()));
        wakeCount.setText("Count: " + mWakelock.getCount());
        progress.setMax(totalTime);
        progress.setProgress((mWakelock.getDuration()*100)/totalTime);
        return row;
    }
}
