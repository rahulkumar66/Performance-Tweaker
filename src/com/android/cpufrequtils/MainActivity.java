package com.android.cpufrequtils;

import java.io.BufferedReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.extras.utils.Constants;
import com.android.extras.utils.CpuUtils;
import com.android.extras.utils.RootUtils;
import com.cpufrequtils.app.R;

public class MainActivity extends Activity implements Constants {
	TextView max, min;
	Button apply, exit;
	Spinner maxSpinner, minSpinner, governorSpinner, IOSchedulers;
	BufferedReader stdinput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!((RootUtils.isRooted()))) {
			Toast.makeText(getBaseContext(),
					"Sorry You do not have Root permissions", Toast.LENGTH_LONG)
					.show();
			Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("Failed");
			builder.setCancelable(false);
			builder.setMessage("You dont have superuser permissions");
			builder.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
			builder.create().show();
		}

		else {
			/*
			 * Populate Gui Components
			 */
			setContentView(R.layout.activity_main);
			max = (TextView) findViewById(R.id.max_frequency);
			min = (TextView) findViewById(R.id.min_frequency);
			apply = (Button) findViewById(R.id.button_apply);
			exit = (Button) findViewById(R.id.button_exit);
			maxSpinner = (Spinner) findViewById(R.id.spinner1);
			minSpinner = (Spinner) findViewById(R.id.min_spinner2);
			governorSpinner = (Spinner) findViewById(R.id.governor_spinner);
			IOSchedulers = (Spinner) findViewById(R.id.disk_io_scheduler);

			String[] availableGovernors = CpuUtils.getAvailableGovernors();
			ArrayAdapter<String> governorAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item,
					availableGovernors);
			governorAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			governorSpinner.setAdapter(governorAdapter);
			governorSpinner.setSelection(governorAdapter.getPosition(CpuUtils
					.getCurrentScalingGovernor()));

			String[] availableFrequencies = CpuUtils.getAvailableFrequencies();
			ArrayAdapter<String> maxFreqAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item,
					availableFrequencies);
			maxFreqAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			maxSpinner.setAdapter(maxFreqAdapter);
			maxSpinner.setSelection(maxFreqAdapter.getPosition(CpuUtils
					.getCurrentMaxFrequeny()));

			ArrayAdapter<String> minFreqAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item,
					availableFrequencies);
			minFreqAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			minSpinner.setAdapter(minFreqAdapter);
			minSpinner.setSelection(minFreqAdapter.getPosition(CpuUtils
					.getCurrentMinFrequency()));

			max.setText(CpuUtils.getCurrentMaxFrequeny() + "");
			min.setText(CpuUtils.getCurrentMinFrequency() + "");

			String schedulers[] = CpuUtils.getAvailableIOScheduler();
			ArrayAdapter<String> scheduler_adapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, schedulers);
			scheduler_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			IOSchedulers.setAdapter(scheduler_adapter);
			IOSchedulers.setSelection(scheduler_adapter.getPosition(CpuUtils
					.getCurrentIOScheduler()));

			/*
			 * set event handlers
			 */
			apply.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							CpuUtils.setFrequencyAndGovernor(maxSpinner
									.getSelectedItem().toString(), minSpinner
									.getSelectedItem().toString(),
									governorSpinner.getSelectedItem()
											.toString(), IOSchedulers
											.getSelectedItem().toString(),
									getBaseContext());
						}
					}).start();

				}
			});

			exit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		}
	}
}
