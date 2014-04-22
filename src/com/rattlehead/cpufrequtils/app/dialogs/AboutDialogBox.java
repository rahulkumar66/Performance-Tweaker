package com.rattlehead.cpufrequtils.app.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.rattlehead.cpufrequtils.app.R;

public class AboutDialogBox extends SherlockDialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
		.setTitle("About")
		.setMessage(getString(R.string.about))
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {

					}
				}).create();

	}

}
