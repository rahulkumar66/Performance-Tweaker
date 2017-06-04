package com.performancetweaker.performancetweaker.app.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.performancetweaker.performancetweaker.app.R;
import com.performancetweaker.performancetweaker.app.ui.fragments.BuildPropEditorFragment;
import com.performancetweaker.performancetweaker.app.ui.fragments.CpuFrequencyFragment;
import com.performancetweaker.performancetweaker.app.ui.fragments.CpuHotplugFragment;
import com.performancetweaker.performancetweaker.app.ui.fragments.GovernorTuningFragment;
import com.performancetweaker.performancetweaker.app.ui.fragments.GpuControlFragment;
import com.performancetweaker.performancetweaker.app.ui.fragments.IOControlFragment;
import com.performancetweaker.performancetweaker.app.ui.fragments.SettingsFragment;
import com.performancetweaker.performancetweaker.app.ui.fragments.TimeInStatesFragment;
import com.performancetweaker.performancetweaker.app.ui.fragments.VirtualMemoryFragment;
import com.performancetweaker.performancetweaker.app.ui.fragments.WakeLocksFragment;
import com.performancetweaker.performancetweaker.app.utils.CPUHotplugUtils;
import com.performancetweaker.performancetweaker.app.utils.GpuUtils;
import com.splunk.mint.Mint;
import com.stericson.RootTools.RootTools;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;

    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private TextView appCompatibilityMessage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this.getApplication(), "64601c31");
        setContentView(R.layout.fragment_main_layout_navbar);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appCompatibilityMessage = (TextView) findViewById(R.id.app_compatibility_status);
        progressBar = (ProgressBar) findViewById(R.id.loading_main);

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        new Task().execute();
    }

    public void populateGui() {
        progressBar.setVisibility(View.GONE);

        //TODO add settings based on whether they are supported or not
        if (GpuUtils.isGpuFrequencyScalingSupported()) {
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_gpu);
            menuItem.setVisible(true);
        }
        if (CPUHotplugUtils.hasCpuHotplug()) {
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_cpu_hotplug);
            menuItem.setVisible(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, toolbar,
                R.string.settings, R.string.settings) {

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
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.main_content, new CpuFrequencyFragment())
                .commitAllowingStateLoss();
        actionBar.setTitle("CPU");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Fragment mfragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_cpu:
                        mfragment = new CpuFrequencyFragment();
                        actionBar.setTitle(getString(R.string.cpu_frequency));
                        break;
                    case R.id.nav_tis:
                        mfragment = new TimeInStatesFragment();
                        actionBar.setTitle(R.string.time_in_state);
                        break;
                    case R.id.nav_iocontrol:
                        mfragment = new IOControlFragment();
                        actionBar.setTitle(getString(R.string.io));
                        break;
                    case R.id.nav_wakelocks:
                        mfragment = new WakeLocksFragment();
                        actionBar.setTitle(getString(R.string.wakelocks));
                        break;
                    case R.id.nav_settings:
                        mfragment = new SettingsFragment();
                        actionBar.setTitle(getString(R.string.settings));
                        break;
                    case R.id.nav_gpu:
                        mfragment = new GpuControlFragment();
                        actionBar.setTitle(getString(R.string.gpu_frequency));
                        break;
                    case R.id.build_prop:
                        mfragment = new BuildPropEditorFragment();
                        actionBar.setTitle(R.string.build_prop);
                        break;
                    case R.id.vm:
                        mfragment = new VirtualMemoryFragment();
                        actionBar.setTitle(getString(R.string.vm));
                        break;
                    case R.id.nav_cpu_hotplug:
                        mfragment = new CpuHotplugFragment();
                        actionBar.setTitle(getString(R.string.cpu_hotplug));
                        break;
                }
                if (mfragment != null) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.enter_anim, R.animator.exit_animation);
                    fragmentTransaction.replace(R.id.main_content, mfragment).commit();
                }
            }
        }, 400);

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().findFragmentByTag(GovernorTuningFragment.TAG) != null
                && getFragmentManager().findFragmentByTag(GovernorTuningFragment.TAG).isVisible()) {
            //To go back to cpu frequency fragment by pressing back button
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_content, new CpuFrequencyFragment())
                    .commit();
        } else {
            Toast.makeText(getBaseContext(), "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }

    private class Task extends AsyncTask<Void, Void, Void> {

        private boolean hasRoot, hasBusyBox;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            hasRoot = RootTools.isAccessGiven();
            hasBusyBox = RootTools.isBusyboxAvailable() || RootTools.findBinary("toybox");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (!hasRoot || !hasBusyBox) {
                progressBar.setVisibility(View.GONE);

                appCompatibilityMessage.setVisibility(View.VISIBLE);
                appCompatibilityMessage
                        .setText(!hasRoot ? "No root access found" : "No Busybox found");

                if (hasRoot) {
                    //TODO redirect to playstore for installing busybox
                    try {
                        startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri
                                        .parse("market://details?id=stericson.busybox")));
                    } catch (ActivityNotFoundException ignored) {
                    }
                }
            } else {
                populateGui();
            }
        }
    }
}
