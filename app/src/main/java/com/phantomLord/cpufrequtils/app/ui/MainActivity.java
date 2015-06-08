package com.phantomLord.cpufrequtils.app.ui;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.adapters.NavigationDrawerListAdapter;
import com.phantomLord.cpufrequtils.app.dialogs.RootNotFoundAlertDialog;
import com.phantomLord.cpufrequtils.app.utils.Constants;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;

public class MainActivity extends ActionBarActivity {
    private Context context;

    private DrawerLayout mDrawerLayout;
    private ListView listView;

    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_layout_navbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       // listView = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        // BugSenseHandler.initAndStartSession(MainActivity.this, "4cdc31a1");
        context = getBaseContext();
        actionBar = getSupportActionBar();
      //  listView.setAdapter(new NavigationDrawerListAdapter(context));
     //   listView.setOnItemClickListener(new DrawerItemClickListener());
     //   listView.setSelection(0);


        if (!(SysUtils.isRooted()))
            new RootNotFoundAlertDialog().show(getSupportFragmentManager(),
                    getString(R.string.app_name));

        this.getFragmentManager().beginTransaction()
                .replace(R.id.main_content, new CpuFrequencyFragment())
                .commit();


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.action_settings,
                R.string.action_settings) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();

    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Fragment mfragment = null;
            String[] fragmentNames = Constants.mFragmentsArray;
            switch (position) {
                case 0:
                    mfragment = new CpuFrequencyFragment();
                    actionBar.setTitle(fragmentNames[position]);
                    break;
                case 1:
                    mfragment = new TimeInStatesFragment();
                    actionBar.setTitle(fragmentNames[position]);
                    break;
                case 2:
                    mfragment = new IOControlFragment();
                    actionBar.setTitle(fragmentNames[position]);
                    break;
                case 3:
                    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    mfragment = new WakeLocksFragment();
                    actionBar.setTitle(fragmentNames[position]);
                    break;
                case 4:
                    mfragment = new SettingsFragment();
                    actionBar.setTitle(getString(R.string.action_settings));
                    break;
            }
            if (mfragment != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_content, mfragment).commit();
            }
            mDrawerLayout.closeDrawer(listView);
        }

    }

}
