package com.rattlehead666.performancetweaker.app.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class BuildPropUtils {

    public static void overwrite(final String oldKey, final String oldValue, final String newKey,
                                 final String newValue) {
        SysUtils.mount(true, "/system");
        final String command="sed 's|" + oldKey + "=" + oldValue + "|"
                + newKey + "=" + newValue + "|g' -i /system/build.prop" + "\n";

        SysUtils.executeRootCommand(new ArrayList<String>() {{
            add(command);
            add("exit" + "\n");
        }});
    }

    public static void addKey(final String key, final String value) {
        SysUtils.mount(true, "/system");
        Log.d("tweaekr","echo " + key + "=" + value + " >> " + Constants.BUILD_PROP + "\n");
        SysUtils.executeRootCommand(new ArrayList<String>() {{
            add("echo " + key + "=" + value + " >> " + Constants.BUILD_PROP + "\n");
            add("exit" + "\n");
        }});
    }

    public static LinkedHashMap<String, String> getProps() {
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        String buildprop = getOutputFromBuildProp();
        String[] values = buildprop.split("\\r?\\n");
        for (String prop : values)
            if (!prop.isEmpty() && !prop.startsWith("#")) {
                String[] line = prop.split("=");

                StringBuilder value = new StringBuilder();
                if (line.length > 1) {
                    for (int i = 1; i < line.length; i++) value.append(line[i]).append("=");
                    value.setLength(value.length() - 1);
                }
                list.put(line.length > 0 ? line[0].trim() : "", value.toString().trim());
            }
        return list;
    }

    public static boolean hasBuildprop() {
        return getProps().size() > 0;
    }

    public static String getOutputFromBuildProp() {

        StringBuilder buffer = new StringBuilder();
        String data = null;
        Process process;
        BufferedReader stdinput;

        File file = new File(Constants.BUILD_PROP);
        if (file.canRead()) {
            try {
                process = Runtime.getRuntime().exec("cat " + Constants.BUILD_PROP);
                stdinput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((data = stdinput.readLine()) != null) {
                    buffer.append(data + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }
        /*
         * try reading the file as root
		 */
        else {
            InputStream inputStream;
            DataOutputStream dos;

            try {
                process = SysUtils.prepareRootShell();
                dos = new DataOutputStream(process.getOutputStream());
                dos.writeBytes("cat " + Constants.BUILD_PROP + "\n");
                dos.flush();
                dos.writeBytes("exit" + "\n");
                dos.flush();
                dos.close();
                if (process.waitFor() == 0) {
                    inputStream = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((data = reader.readLine()) != null) {
                        buffer.append(data + "\n");
                    }
                }
            } catch (IOException | InterruptedException ioe) {
                ioe.printStackTrace();
            }
            return buffer.toString();
        }
    }
}
