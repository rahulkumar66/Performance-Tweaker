package com.phantomLord.cpufrequtils.app.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.phantomLord.cpufrequtils.app.R;

public class AboutDialogBox extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.about))
                .setMessage(getString(R.string.about_content))
                .setPositiveButton(getString(R.string.okbutton),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        }).create();

    }

}
