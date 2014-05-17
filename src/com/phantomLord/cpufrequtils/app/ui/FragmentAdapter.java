package com.phantomLord.cpufrequtils.app.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class TestFragmentAdapter extends FragmentStatePagerAdapter {
	private int mCount = 3;

	public TestFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new TimeInStatesFragment();
		case 1:
			return new CpuFrequencyFragment();
		case 2:
			return new DiskFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return "Time in State";
		case 1:
			return "Cpu Control";
		case 2:
			return "Disk Control";
		}
		return "";
	}

}
