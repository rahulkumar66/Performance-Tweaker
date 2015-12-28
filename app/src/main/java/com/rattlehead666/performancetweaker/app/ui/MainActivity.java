package com.rattlehead666.performancetweaker.app.ui;

import android.app.Fragment;
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
import com.rattlehead666.performancetweaker.app.R;
import com.rattlehead666.performancetweaker.app.utils.GpuUtils;
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

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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

    //TODO shed
    if (GpuUtils.isGpuFrequencyScalingSupported()) {
      navigationView.getMenu().getItem(1).setVisible(true);
    }

    mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, toolbar,
        R.string.action_settings, R.string.action_settings) {

      @Override public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
      }

      @Override public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
      }
    };
    mDrawerToggle.syncState();
    mDrawerLayout.setDrawerListener(mDrawerToggle);
    navigationView.setNavigationItemSelectedListener(this);

    getFragmentManager().beginTransaction()
        .replace(R.id.main_content, new CpuFrequencyFragment())
        .commit();
    actionBar.setTitle("CPU");
  }

  @Override public boolean onNavigationItemSelected(final MenuItem menuItem) {

    new Handler().postDelayed(new Runnable() {
      @Override public void run() {

        Fragment mfragment = null;

        switch (menuItem.getItemId()) {
          case R.id.nav_cpu:
            mfragment = new CpuFrequencyFragment();
            actionBar.setTitle("CPU");
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
          getFragmentManager().beginTransaction().replace(R.id.main_content, mfragment).commit();
        }
      }
    }, 400);

    mDrawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  private class Task extends AsyncTask<Void, Void, Void> {

    private boolean hasRoot, hasBusyBox;

    @Override protected void onPreExecute() {
      super.onPreExecute();

      progressBar.setVisibility(View.VISIBLE);
    }

    @Override protected Void doInBackground(Void... voids) {

      hasRoot = RootTools.isAccessGiven();
      hasBusyBox = RootTools.isBusyboxAvailable();

      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);

      if (!hasRoot || !hasBusyBox) {
        progressBar.setVisibility(View.GONE);

        appCompatibilityMessage.setVisibility(View.VISIBLE);
        appCompatibilityMessage.setText(
            !hasRoot ? "No root access found" : "No Busybox found");

        if (hasRoot) {
          //redirect to playstore for installing busybox

          try {
            startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=stericson.busybox")));
          } catch (ActivityNotFoundException ignored) {
          }
        }
      } else {
        populateGui();
      }
    }
  }
}
