package com.phantomLord.cpufrequtils.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.phantomLord.cpufrequtils.app.R;
import com.phantomLord.cpufrequtils.app.dialogs.AboutDialogBox;
import com.phantomLord.cpufrequtils.app.dialogs.RootNotFoundAlertDialog;
import com.phantomLord.cpufrequtils.app.utils.RootUtils;

public class MainActivity extends SherlockFragmentActivity {
	private static final int CONTENT_VIEW_ID = 666;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(MainActivity.this, "4cdc31a1");
		FrameLayout frame = new FrameLayout(this);
		frame.setId(CONTENT_VIEW_ID);
		setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		if (savedInstanceState == null) {
			setInitialFragment();
		}

		if (!(RootUtils.isRooted()))
			new RootNotFoundAlertDialog().show(getSupportFragmentManager(),
					"Cpu Tuner");

	}

	private void setInitialFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(CONTENT_VIEW_ID, MainFragment.newInstance())
				.commit();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		String title = item.getTitle().toString();
		if (title.equals(getString(R.string.report_to_github))) {
			Uri uri = Uri.parse(getString(R.string.github_bugtracker));
			Intent mIntent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(mIntent);
		} else if (title.equals("Settings")) {
			startActivity(new Intent(getBaseContext(), SettingsActivity.class));
		} else if (title.equals("About")) {
			new AboutDialogBox().show(getSupportFragmentManager(), title);
		}

		return super.onMenuItemSelected(featureId, item);
	}

}
