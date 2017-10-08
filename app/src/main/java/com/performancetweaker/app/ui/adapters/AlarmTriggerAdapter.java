package com.performancetweaker.app.ui.adapters;

import com.asksven.android.common.nameutils.UidNameResolver;
import com.asksven.android.common.privateapiproxies.Alarm;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.performancetweaker.app.R;
import com.performancetweaker.app.utils.BatteryStatsUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AlarmTriggerAdapter extends BaseAdapter {

    private ArrayList<Alarm> alarms;
    private Context context;
    private int totaltime;
    private LayoutInflater inflater;
    private UidNameResolver uidNameResolver;

    public AlarmTriggerAdapter(Context ctx) {
        this.context = ctx;
        alarms = BatteryStatsUtils.getInstance(context).getAlarmStats();
        totaltime = 0;
        for (Alarm e : alarms) {
            totaltime += e.getWakeups();
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        uidNameResolver = UidNameResolver.getInstance(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        View row = inflater.inflate(R.layout.alarm_trigger_custom_list_item1, container, false);
        TextView AlarmPackageName = row.findViewById(R.id.alarm_package_name);
        TextView WakeupCount = row.findViewById(R.id.wakeup_count);
        TextView name = row.findViewById(R.id.alarm_appname);
        ImageView icon = row.findViewById(R.id.alarm_icon);
        DonutProgress progress = row.findViewById(R.id.donut_progress);
        Alarm alarm = alarms.get(position);

        icon.setImageDrawable(alarm.getIcon(uidNameResolver));
        String packageName = alarm.getPackageName();
        AlarmPackageName.setText(uidNameResolver.getLabel(packageName));
        name.setText(packageName);
        WakeupCount.setText("x" + alarm.getWakeups() + " times");
        progress.setMax(100);
        progress.setProgress(alarm.getWakeups() * 100 / totaltime);
        return row;
    }

    @Override
    public int getCount() {
        if (alarms != null) {
            return alarms.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int arg0) {
        return alarms.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return alarms.indexOf(arg0);
    }
}
