package com.rattlehead.cpufrequtils.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.rattlehead.cpufrequtils.app.dialogs.AboutDialogBox;
import com.rattlehead.cpufrequtils.app.dialogs.RootNotFoundAlertDialog;
import com.rattlehead.cpufrequtils.app.utils.RootUtils;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class MainActivity extends SherlockFragmentActivity {

	TestFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(MainActivity.this, "4cdc31a1");
		setContentView(R.layout.title_tabs);

		mAdapter = new TestFragmentAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		if (!(RootUtils.isRooted()))
			new RootNotFoundAlertDialog().show(getSupportFragmentManager(),
					"Cpu Tuner");

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		String title = item.getTitle().toString();
		if (title.equals(getString(R.string.report_to_github))) {
			Uri uri = Uri.parse(getString(R.string.github_bugtracker));
			Intent mIntent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(mIntent);	
		} 
		else if (title.equals("Settings")) {
			startActivity(new Intent(getBaseContext(), SettingsActivity.class));
		} 
		else if (title.equals("About")) {
			new AboutDialogBox().show(getSupportFragmentManager(), title);
		}

		return super.onMenuItemSelected(featureId, item);
	}

}
