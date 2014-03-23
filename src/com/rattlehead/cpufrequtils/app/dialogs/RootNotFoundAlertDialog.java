package com.rattlehead.cpufrequtils.app.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.rattlehead.cpufrequtils.app.R;

public class RootNotFoundAlertDialog extends SherlockDialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setIcon(R.drawable.ic_launcher)
				.setTitle("Error")
				.setMessage("Your Phone Does Not Have Root Access")
				.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						})
				.setNegativeButton("Exit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								getActivity().finish();
							}
						}).create();
	}
}
