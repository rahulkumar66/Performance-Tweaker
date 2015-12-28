package com.rattlehead666.performancetweaker.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import app.phantomLord.cpufrequtils.app.R;

public class GovernorTuningActivity extends AppCompatActivity {

  Toolbar toolBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_governor_tune_preference);

    toolBar = (Toolbar) findViewById(R.id.toolbar_preference);

    setSupportActionBar(toolBar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getFragmentManager().beginTransaction()
        .replace(R.id.frame_layout_preference, new GovernorTuningFragment())
        .commit();
  }
}
