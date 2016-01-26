package com.rattlehead666.performancetweaker.app.ui.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rattlehead666.performancetweaker.app.R;

public class PreferenceContainerFragment extends Fragment {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fragment prefsFragment = new PreferenceContainerFragment();
    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
    // transaction.add(R.id.child_fragment, prefsFragment).commit();

  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    return inflater.inflate(R.layout.fragment_pref_container, container, false);
  }
}