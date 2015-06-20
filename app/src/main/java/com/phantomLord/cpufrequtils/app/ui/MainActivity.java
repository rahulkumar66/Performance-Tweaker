package com.phantomLord.cpufrequtils.app.ui;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.dialogs.RootNotFoundAlertDialog;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;

public class MainActivity extends ActionBarActivity {
    private Context context;

    private DrawerLayout mDrawerLayout;

    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar actionBar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_layout_navbar);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // BugSenseHandler.initAndStartSession(MainActivity.this, "4cdc31a1");
        context = getBaseContext();
        actionBar = getSupportActionBar();

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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment mfragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_cpu:
                        mfragment = new CpuFrequencyFragment();
                        actionBar.setTitle("Cpu Frequency");
                        break;
                    case R.id.nav_tis:
                        mfragment = new TimeInStatesFragment();
                        actionBar.setTitle("Time In State");
                        break;
                    case R.id.nav_iocontrol:
                        mfragment = new IOControlFragment();
                        actionBar.setTitle("I/O Control");
                        break;
                    case R.id.nav_wakelocks:
                        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                        mfragment = new WakeLocksFragment();
                        actionBar.setTitle("Wakelocks");
                        break;
                    case R.id.nav_settings:
                        mfragment = new SettingsFragment();
                        actionBar.setTitle(getString(R.string.action_settings));
                        break;
                    case R.id.nav_gpu:
                        mfragment = new GpuControlFragment();
                        actionBar.setTitle("GPU Settings");
                        break;
                }
                if (mfragment != null) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.main_content, mfragment)
                            .commit();
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });
    }
}