package com.phantomLord.cpufrequtils.app.ui;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconCompat;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.adapters.NavigationDrawerListAdapter;
import com.phantomLord.cpufrequtils.app.dialogs.RootNotFoundAlertDialog;
import com.phantomLord.cpufrequtils.app.utils.Constants;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;

public class MainActivity extends ActionBarActivity {
    private Context themedContext, context;

    private boolean isLight;
    private String key;
    private boolean isDrawerOpened;

    private DrawerLayout mDrawerLayout;
    private ListView listView;

    private ActionBar actionBar;

    private MaterialMenuIconCompat materialMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences mPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        key = mPrefs.getString("listPref", "Light");
        this.setTheme(Constants.THEMES_MAP.get(key));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_layout_navbar);

        // BugSenseHandler.initAndStartSession(MainActivity.this, "4cdc31a1");
        themedContext = getSupportActionBar().getThemedContext();
        actionBar = getSupportActionBar();
        context = getBaseContext();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.left_drawer);
        if(Constants.THEMES_MAP.get(key).equals(R.style.Theme_AppCompat) ||
                Constants.THEMES_MAP.get(key).equals(R.style.Theme_AppCompat_Light_DarkActionBar) ) {
            materialMenu = new MaterialMenuIconCompat(this, Color.WHITE,
                    MaterialMenuDrawable.Stroke.THIN);
        }
        else {
            materialMenu = new MaterialMenuIconCompat(this, Color.DKGRAY,
                    MaterialMenuDrawable.Stroke.THIN);
        }
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        listView.setAdapter(new NavigationDrawerListAdapter(themedContext));
        listView.setOnItemClickListener(new DrawerItemClickListener());
        listView.setSelection(0);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        if (!(SysUtils.isRooted()))
            new RootNotFoundAlertDialog().show(getSupportFragmentManager(),
                    getString(R.string.app_name));

        this.getFragmentManager().beginTransaction()
                .replace(R.id.main_content, new CpuFrequencyFragment())
                .commit();


        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                materialMenu.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        isDrawerOpened ? 2 - slideOffset : slideOffset
                );
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpened = false;
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    if (isDrawerOpened)
                        materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    else
                        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        materialMenu.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!(isDrawerOpened)) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            } else if (isDrawerOpened) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        isDrawerOpened = mDrawerLayout.isDrawerOpen(Gravity.START); // or END, LEFT, RIGHT
        materialMenu.syncState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
