package com.rattlehead.cpufrequtils.app;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.bugsense.trace.BugSenseHandler;
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

}
