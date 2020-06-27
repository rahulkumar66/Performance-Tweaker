package com.performancetweaker.app.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.performancetweaker.app.R;
import com.performancetweaker.app.ui.fragments.BuildPropEditorFragment;
import com.performancetweaker.app.ui.fragments.CpuFrequencyFragment;
import com.performancetweaker.app.ui.fragments.CpuHotplugFragment;
import com.performancetweaker.app.ui.fragments.GovernorTuningFragment;
import com.performancetweaker.app.ui.fragments.GpuControlFragment;
import com.performancetweaker.app.ui.fragments.IOControlFragment;
import com.performancetweaker.app.ui.fragments.SettingsFragment;
import com.performancetweaker.app.ui.fragments.TimeInStatesFragment;
import com.performancetweaker.app.ui.fragments.VirtualMemoryFragment;
import com.performancetweaker.app.utils.CPUHotplugUtils;
import com.performancetweaker.app.utils.Constants;
import com.performancetweaker.app.utils.InterstialHelper;
import com.performancetweaker.app.utils.GpuUtils;
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
    private GpuUtils gpuUtils;
    private InterstialHelper interstialHelper;
    private FirebaseAnalytics firebaseAnalytics;

    private String TAG = Constants.App_Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_layout_navbar);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        navigationView = findViewById(R.id.navigation);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        appCompatibilityMessage = findViewById(R.id.app_compatibility_status);
        progressBar = findViewById(R.id.loading_main);
//        bannerContainer = findViewById(R.id.ad_container);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //disable the navigation bar initially
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

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

        new RunOnInitTask().execute();
        String installer = getBaseContext().getPackageManager().getInstallerPackageName(getBaseContext().getPackageName());
        firebaseAnalytics.setUserProperty("installer_source", installer == null ? "invalid_source" : installer);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        Log.d(TAG, "FCM TOKEN:" + task.getResult().getToken());
                    }
                });
    }

    private void initAds(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public void populateGui() {
        //enable navigation drawer
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        actionBar.setTitle("CPU");

        gpuUtils = GpuUtils.getInstance();

        //TODO add settings based on whether they are supported or not
        if (gpuUtils.isGpuFrequencyScalingSupported()) {
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_gpu);
            menuItem.setVisible(true);
        }
        if (CPUHotplugUtils.hasCpuHotplug()) {
            MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_cpu_hotplug);
            menuItem.setVisible(true);
        }

        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_cpu));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = null;
                Bundle bundle = new Bundle();

                switch (menuItem.getItemId()) {
                    case R.id.nav_cpu:
                        fragment = new CpuFrequencyFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.cpu_frequency));
                        actionBar.setTitle(R.string.cpu_frequency);
                        break;
                    case R.id.nav_tis:
                        fragment = new TimeInStatesFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.time_in_state));
                        actionBar.setTitle(R.string.time_in_state);
                        break;
                    case R.id.nav_iocontrol:
                        fragment = new IOControlFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.io));
                        actionBar.setTitle(R.string.io);
                        break;
                    case R.id.nav_settings:
                        fragment = new SettingsFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.settings));
                        actionBar.setTitle(R.string.settings);
                        break;
                    case R.id.nav_gpu:
                        fragment = new GpuControlFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.gpu_frequency));
                        actionBar.setTitle(R.string.gpu_frequency);
                        break;
                    case R.id.build_prop:
                        fragment = new BuildPropEditorFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.build_prop));
                        actionBar.setTitle(R.string.build_prop);
                        break;
                    case R.id.vm:
                        fragment = new VirtualMemoryFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.vm));
                        actionBar.setTitle(R.string.vm);
                        break;
                    case R.id.nav_cpu_hotplug:
                        fragment = new CpuHotplugFragment();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.cpu_hotplug));
                        actionBar.setTitle(R.string.cpu_hotplug);
                        break;
                }
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.enter_anim, R.animator.exit_animation);
                    fragmentTransaction.replace(R.id.main_content, fragment).commitAllowingStateLoss();
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.main_content, new CpuFrequencyFragment())
//                    .commit();
        } else {
            //  Toast.makeText(getBaseContext(), "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }

    private class RunOnInitTask extends AsyncTask<Void, Void, Void> {
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
            initAds(MainActivity.this);
            interstialHelper = InterstialHelper.getInstance(getBaseContext(), true);

            if (hasRoot && hasBusyBox) {
                populateGui();
            } else {
                progressBar.setVisibility(View.GONE);

                appCompatibilityMessage.setVisibility(View.VISIBLE);
                appCompatibilityMessage
                        .setText(!hasRoot ? "No root access found" : "No Busybox found");

                if (hasRoot) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=stericson.busybox")));
                    } catch (ActivityNotFoundException ignored) {
                    }
                }
            }
        }
    }
}
