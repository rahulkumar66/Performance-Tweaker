package com.rattlehead.cpufrequtils.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.rattlehead.cpufrequtils.app.utils.CpuUtils;
import com.rattlehead.cpufrequtils.app.utils.RootUtils;

public class CpuControlFragment extends SherlockFragment {
	View mView;
	Context mContext;
	Activity mActivity;
	TextView max, min;
	Button apply, exit;
	Spinner maxSpinner, minSpinner, governorSpinner, IOSchedulers;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.cpu_freqs, container, false);

		mActivity = getSherlockActivity();
		mContext = getSherlockActivity().getBaseContext();

		if (((RootUtils.isRooted()))) {
			/*
			 * Populate Gui Components
			 */
			getRefrencesToUiElements();
			populateGui();

		} else {
	/*		Toast.makeText(mContext, "You do not have Root permissions",
					Toast.LENGTH_LONG).show();
			Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("Failed");
			builder.setCancelable(false);
			builder.setMessage("You dont have superuser permissions");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							mActivity.finish();
						}
					});
			builder.create().show();
			
			*/
		}
		apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						CpuUtils.setFrequencyAndGovernor(maxSpinner.getSelectedItem()
								.toString(), minSpinner.getSelectedItem().toString(),
								governorSpinner.getSelectedItem().toString(),
								IOSchedulers.getSelectedItem().toString(), mContext);
					}
				}).start();
				
				}
		});

		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});

		return mView;
	}

	private void populateGui() {
		String[] availableGovernors = CpuUtils.getAvailableGovernors();
		ArrayAdapter<String> governorAdapter = new ArrayAdapter<String>(
				mContext, android.R.layout.simple_spinner_item,
				availableGovernors);
		governorAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		governorSpinner.setAdapter(governorAdapter);
		governorSpinner.setSelection(governorAdapter.getPosition(CpuUtils
				.getCurrentScalingGovernor()));

		String[] availableFrequencies = CpuUtils.getAvailableFrequencies();
		ArrayAdapter<String> maxFreqAdapter = new ArrayAdapter<String>(
				mContext, android.R.layout.simple_spinner_item,
				availableFrequencies);
		maxFreqAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		maxSpinner.setAdapter(maxFreqAdapter);
		maxSpinner.setSelection(maxFreqAdapter.getPosition(CpuUtils
				.getCurrentMaxFrequeny()));

		ArrayAdapter<String> minFreqAdapter = new ArrayAdapter<String>(
				mContext, android.R.layout.simple_spinner_item,
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
				mContext, android.R.layout.simple_spinner_item, schedulers);
		scheduler_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		IOSchedulers.setAdapter(scheduler_adapter);
		IOSchedulers.setSelection(scheduler_adapter.getPosition(CpuUtils
			.getCurrentIOScheduler()));

	}

	private void getRefrencesToUiElements() {

		max = (TextView) mView.findViewById(R.id.max_frequency);
		min = (TextView) mView.findViewById(R.id.min_frequency);
		apply = (Button) mView.findViewById(R.id.button_apply);
		exit = (Button) mView.findViewById(R.id.button_exit);
		maxSpinner = (Spinner) mView.findViewById(R.id.spinner1);
		minSpinner = (Spinner) mView.findViewById(R.id.min_spinner2);
		governorSpinner = (Spinner) mView.findViewById(R.id.governor_spinner);
		IOSchedulers = (Spinner) mView.findViewById(R.id.disk_io_scheduler);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

}
