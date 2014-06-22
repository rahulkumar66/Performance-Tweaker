package com.phantomLord.cpufrequtils.app.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.phantomLord.cpufrequtils.app.R;
import com.sherlock.navigationdrawer.compat.SherlockActionBarDrawerToggle;

public class MainFragment extends SherlockFragment {

	private DrawerLayout mDrawerLayout;
	private ListView listView;

	private ActionBarHelper actionBar;

	private SherlockActionBarDrawerToggle mDrawerToggle;

	public static Fragment newInstance() {
		Fragment f = new MainFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_layout, container,
				false);

		mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		listView = (ListView) view.findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		listView.setAdapter(new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_list_item_1, new String[] {
						"Time In State", "Cpu Frequency", "SD Storage",
						"Wakelocks" }));
		listView.setOnItemClickListener(new DrawerItemClickListener());
		listView.setCacheColorHint(0);
		listView.setScrollingCacheEnabled(false);
		listView.setScrollContainer(false);
		listView.setFastScrollEnabled(true);
		listView.setSmoothScrollbarEnabled(true);

		actionBar = createActionBarHelper();
		actionBar.init();

		// ActionBarDrawerToggle provides convenient helpers for tying together
		// the
		// prescribed interactions between a top-level sliding drawer and the
		// action bar.
		mDrawerToggle = new SherlockActionBarDrawerToggle(this.getActivity(),
				mDrawerLayout, R.drawable.ic_drawer_light, R.string.about,
				R.string.about_content);
		mDrawerToggle.syncState();
		Fragment timeInstateFragment = new TimeInStatesFragment();
		getSherlockActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.linearlay1, timeInstateFragment).commit();

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater = ((SherlockFragmentActivity) getActivity())
				.getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
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
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * This list item click listener implements very simple view switching by
	 * changing the primary content text. The drawer is closed when a selection
	 * is made.
	 */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Fragment mfragment = new Fragment();
			switch (position) {
			case 0:
				mfragment = new TimeInStatesFragment();
				actionBar.mActionBar.setTitle("Time In States");
				break;
			case 1:
				mfragment = new CpuFrequencyFragment();
				actionBar.mActionBar.setTitle("Cpu Frequency");
				break;
			case 2:
				mfragment = new DiskFragment();
				actionBar.mActionBar.setTitle("SD Storage");
				break;
			case 3:
				mfragment = new WakeLocksDetectorFragment();
				actionBar.mActionBar.setTitle("WakeLocks");
				break;
			}
			FragmentManager fm = getSherlockActivity()
					.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.linearlay1, mfragment);
		//	ft.addToBackStack("tag");
			ft.commit();
			mDrawerLayout.closeDrawer(listView);
		}
	}

	/**
	 * Create a compatible helper that will manipulate the action bar if
	 * available.
	 */
	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper();
	}

	private class ActionBarHelper {
		private final ActionBar mActionBar;

		private ActionBarHelper() {
			mActionBar = ((SherlockFragmentActivity) getActivity())
					.getSupportActionBar();
		}

		public void init() {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(true);
		}

		/**
		 * When the drawer is closed we restore the action bar state reflecting
		 * the specific contents in view.
		 */
	}

}
