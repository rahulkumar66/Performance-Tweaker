package com.rattlehead.cpufrequtils.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class TestFragmentAdapter extends FragmentPagerAdapter {	    
    private int mCount = 3;

    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    
    @Override
    public Fragment getItem(int position) {
    	switch(position) {
    	case 0:
    		return new CpuInformation();
    	case 1:
    		return new CpuControlFragment();
    	case 2:
    		return new TimeInStatesFragment();
    	}
    	return null;
    }

    
    @Override
    public int getCount() {
        return mCount;
    }
    
    
    @Override
    public CharSequence getPageTitle(int position) {
    	switch(position) {
    	case 0:
    		return "Cpu Information";
    	case 1:
			return "Cpu Control";
    	case 2:
    		return "Time In State";
    	}
    	return "";	
    }
    
}

