package com.phantomLord.cpufrequtils.app.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.stericson.RootTools.RootTools;

import java.util.ArrayList;

public class SystemAppUtilities {

    private static String privAppFile = "/system/priv-app/performancetweaker.apk";

    private static String getAPKName(Context ctx, boolean includeFullPath, boolean doWildCard) throws SystemAppManagementException {
        String fullPath = ctx.getApplicationInfo().sourceDir;
        if (fullPath.isEmpty() || (fullPath.lastIndexOf('/') == -1)) {
            throw new SystemAppManagementException("Unable to find the path to the APK.  Is it already uninstalled?  Did you remember to reboot after uninstalling?  Current location appears to be: " + fullPath);
        }
        if (!includeFullPath) {
            fullPath = fullPath.substring(fullPath.lastIndexOf('/') + 1);
        }
        if (doWildCard) {
            int indexOfHyphen = fullPath.lastIndexOf('-');
            if (indexOfHyphen > 0) {
                return fullPath.substring(0, indexOfHyphen) + "*";
            }
        }
        return fullPath;
    }

    public static boolean hasBatteryStatsPermission(Context ctx) {
        return (PackageManager.PERMISSION_GRANTED == ctx.getPackageManager().checkPermission("android.permission.BATTERY_STATS", ctx.getPackageName()));
    }

    public static void installAsSystemApp(final Context ctx) throws SystemAppManagementException {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            SystemAppManagementException error = null;
            ProgressDialog progress = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress = ProgressDialog.show(ctx, "Please Wait", "Copying App to System");
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                // Verify we do have root
                if (!RootTools.isAccessGiven()) {
                    return false;
                }

                // Copy the file to /system/priv-app
                String currentFile;
                try {
                    currentFile = getAPKName(ctx, true, false);
                } catch (SystemAppManagementException e) {
                    error = e;
                    return false;
                }
                boolean copiedApp = RootTools.copyFile(currentFile, privAppFile, true, true);
                Log.d(Constants.App_Tag, "Used RootTools to copy app from: " + currentFile + ", to: " + privAppFile + ".  Was it successful? " + copiedApp);

                if (!copiedApp) {
                    error = new SystemAppManagementException("Unable to copy the file \"" + currentFile + "\" to \"" + privAppFile + "\".  You may need to try this manually using a tool such as Root Explorer.");
                    return false;
                }
                //TODO install backup script on cm based roms
                //installBackupScript(ctxt);


                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                progress.dismiss();

                if (result) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setMessage("Reboot to Apply Changes")
                            .setTitle("Complete")
                            .setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO reboot
                                    rebootDevice(((Dialog) dialog).getContext());
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    String message = (error != null) ? error.getMessage() : "Unknown Error";
                    builder.setMessage(message)
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                }
            }
        };
        task.execute((Void) null);
    }

    public static void rebootDevice(final Context context) {

        AsyncTask<Void, Void, Boolean> rebootTask = new AsyncTask<Void, Void, Boolean>() {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "Rebooting", "Please Wait");
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                ArrayList<String> rebootCommand = new ArrayList<>();
                rebootCommand.add("reboot \n");
                rebootCommand.add("exit \n");
                SysUtils.executeRootCommand(rebootCommand);
                return null;
            }

            @Override
            protected void onPostExecute(Boolean status) {
                super.onPostExecute(status);

                progressDialog.dismiss();

                if (!status) {
                    Log.d(Constants.App_Tag, "Restarting phone via rootTools as reboot failed...");
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Unable to reboot automatically . Please reboot your phone manually.")
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                }
            }
        };
        rebootTask.execute();

    }


}
