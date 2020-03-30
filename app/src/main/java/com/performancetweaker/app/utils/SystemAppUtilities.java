package com.performancetweaker.app.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.asksven.android.common.RootShell;
import com.stericson.RootTools.RootTools;

import java.util.ArrayList;
import java.util.List;

public class SystemAppUtilities {

    private static final String SYSTEM_DIR_4_4 = "/system/priv-app/";
    private static final String SYSTEM_DIR = "/system/app/";

    public static String getAPKName(Context ctx, boolean includeFullPath, boolean doWildCard)
            throws SystemAppManagementException {
        String fullPath = ctx.getApplicationInfo().sourceDir;
        if (fullPath.isEmpty() || (fullPath.lastIndexOf('/') == -1)) {
            throw new com.performancetweaker.app.utils.SystemAppManagementException(
                    "Unable to find the path to the APK.  Is it already uninstalled?  Did you remember to reboot after uninstalling?  Current location appears to be: "
                            + fullPath);
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

    public static void installAsSystemApp(final Context ctx) {
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
                boolean copiedApp = installAsSystemApp(currentFile);

                Log.d(Constants.App_Tag, "Copying app to system directory"
                        + ".  Was it successful? "
                        + copiedApp);

                if (!copiedApp) {
                    error = new SystemAppManagementException("Unable to copy the file \""
                            + currentFile
                            + "\" to \""
                            + "\".  You may need to try this manually using a tool such as Root Explorer.");
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
                                    rebootDevice(((Dialog) dialog).getContext());
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    String message = (error != null) ? error.getMessage() : "Unknown Error";
                    builder.setMessage(message).setNeutralButton(android.R.string.ok, null).show();
                }
            }
        };
        task.execute();
    }

    private static void rebootDevice(final Context context) {

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
                rebootCommand.add("reboot");
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

    private static boolean installAsSystemApp(String apk) {
        ArrayList<String> command = new ArrayList<>();
        command.add("mount -o rw,remount /system");

        if (Build.VERSION.SDK_INT >= 19) {
            command.add("cp " + apk + " " + SYSTEM_DIR_4_4 + "performancetweaker.apk");
            command.add("chmod 644 " + SYSTEM_DIR_4_4 + "performancetweaker.apk");
        } else {
            command.add("cp " + apk + " " + SYSTEM_DIR + "performancetweaker.apk");
            command.add("chmod 644 "+ SYSTEM_DIR+ "performancetweaker.apk");
        }

        return SysUtils.executeRootCommand(command);
    }

    public static boolean uninstallAsSystemApp(String apk) {
        ArrayList<String> command = new ArrayList<>();
        command.add("mount -o rw,remount /system");

        if (Build.VERSION.SDK_INT >= 19) {
            command.add("rm"+" "+SYSTEM_DIR_4_4 + "performancetweaker.apk");
        } else {
            command.add("rm"+" "+ SYSTEM_DIR + "performancetweaker.apk");
        }

        return SysUtils.executeRootCommand(command);
    }

    public static boolean isSystemApp(String apk)
    {
        boolean ret = false;
        List<String> res;

        String command = "";
        if (Build.VERSION.SDK_INT >= 19)
        {
            command = "ls " + SYSTEM_DIR_4_4 + "/" + apk;
        }
        else
        {
            command = "ls " + SYSTEM_DIR + "/" + apk;
        }

        res = RootShell.getInstance().run(command);

        if (res.size() > 0)
        {
            ret = !res.get(0).contains("No such file or directory");
        }

        return ret;
    }
}
