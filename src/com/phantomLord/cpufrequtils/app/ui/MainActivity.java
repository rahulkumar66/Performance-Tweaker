package com.phantomLord.cpufrequtils.app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.adapters.CpuControlActionBarSpinner;
import com.phantomLord.cpufrequtils.app.adapters.NavigationDrawerListAdapter;
import com.phantomLord.cpufrequtils.app.dialogs.AboutDialogBox;
import com.phantomLord.cpufrequtils.app.dialogs.RootNotFoundAlertDialog;
import com.phantomLord.cpufrequtils.app.utils.Constants;
import com.phantomLord.cpufrequtils.app.utils.SysUtils;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

public class MainActivity extends SherlockFragmentActivity {
	Context themedContext, context;
	boolean isLight;
	String key;

	private DrawerLayout mDrawerLayout;
	private ListView listView;

	private ActionBar actionBar;

	private SherlockActionBarDrawerToggle mDrawerToggle;

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
		context = getBaseContext();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listView = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		listView.setAdapter(new NavigationDrawerListAdapter(context));
		listView.setOnItemClickListener(new DrawerItemClickListener());
		listView.setCacheColorHint(0);
		listView.setScrollingCacheEnabled(false);
		listView.setScrollContainer(false);
		listView.setFastScrollEnabled(true);
		listView.setSmoothScrollbarEnabled(true);
		listView.setSelection(0);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		mDrawerToggle = new SherlockActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer_light, R.string.about,
				R.string.about_content);
		mDrawerToggle.syncState();
		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_content, new CpuFrequencyFragment())
				.commit();

		if (!(SysUtils.isRooted()))
			new RootNotFoundAlertDialog().show(getSupportFragmentManager(),
					"Performance Tweaker");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up action should open or close the drawer.
		 * mDrawerToggle will take care of this.
		 */
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		String title = item.getTitle().toString();
		if (title.equals(getString(R.string.report_to_github))) {
			Uri uri = Uri.parse(getString(R.string.github_bugtracker));
			Intent mIntent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(mIntent);
		} else if (title.equals("Settings")) {
			startActivity(new Intent(getBaseContext(), PreferenceActivity.class));
		} else if (title.equals("About")) {
			new AboutDialogBox().show(getSupportFragmentManager(), title);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		if (key.equals("Light")) {
			MenuItem myMenuItem = menu.findItem(R.id.overflow_menu1);
			myMenuItem
					.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_light);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener, OnNavigationListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Fragment mfragment = null;
			switch (position) {
			case 0:
				mfragment = new CpuFrequencyFragment();
				actionBar.setTitle("Cpu Frequency");
				break;
			case 1:
				mfragment = new TimeInStatesFragment();
				actionBar.setTitle("Time In State");
				break;
			case 2:
				mfragment = new IOControlFragment();
				actionBar.setTitle("I/O Control");
				break;
			case 3:
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				mfragment = new WakeLocksDetectorFragment();
				actionBar.setTitle("");
				break;
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_content, mfragment).commit();

			mDrawerLayout.closeDrawer(listView);
		}

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			// TODO Auto-generated method stub
			return true;
		}
	}

}
