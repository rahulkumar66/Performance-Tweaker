package com.android.cpufrequtils;

import java.io.BufferedReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.extras.SysUtils;
import com.cpufrequtils.app.R;

public class MainActivity extends Activity {
	TextView max, min;
	Button apply, exit;
	Spinner maxSpinner, minSpinner;
	String tag = "cpu control";
	BufferedReader stdinput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!((SysUtils.isRooted()))) {
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

			String[] availableFrequencies = SysUtils.getAvailableFrequencies();
			ArrayAdapter<String> maxFreqAdapter = new ArrayAdapter<String>(
					getBaseContext(), android.R.layout.simple_spinner_item,
					availableFrequencies);
			maxFreqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			maxSpinner.setAdapter(maxFreqAdapter);
			maxSpinner.setSelection(SysUtils.getCurrentMaxFrequencyIndex(availableFrequencies));
			
			ArrayAdapter<String> minFreqAdapter=new ArrayAdapter<String>(
					getBaseContext(),android.R.layout.simple_spinner_item,
					availableFrequencies);
			minFreqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			minSpinner.setAdapter(minFreqAdapter);
			minSpinner.setSelection(SysUtils.getCurrentMinFrequencyIndex(availableFrequencies));

			max.setText(SysUtils.getCurrentMaxFrequeny() + "");
			min.setText(SysUtils.getCurrentMinFrequency() + "");

			/*
			 * set event handlers
			 */
			apply.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							SysUtils.setFrequencyAndGovernor(maxSpinner.getSelectedItem().toString(),
									minSpinner.getSelectedItem().toString());		
						}
					}).start();
					
					Toast.makeText(getBaseContext(),
							"Maximum Frequency "
									+ SysUtils.getCurrentMaxFrequeny()+" Minimum Frequency "
									+SysUtils.getCurrentMinFrequency(),
							Toast.LENGTH_SHORT).show();
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
