package com.rattlehead666.performancetweaker.app.utils;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VmUtils {

  private static List<String> vmEntries = new ArrayList<>();

  public static void setVM(String value, String name, Context context) {
    // Control.runCommand(value, VM_PATH + "/" + name, Control.CommandType.GENERIC, context);
  }

  public static String getVMValue(String file) {
    if (new File(Constants.VM_PATH + "/" + file).exists()) {
      String value = SysUtils.readOutputFromFile(Constants.VM_PATH + "/" + file);
      if (value != null) return value;
    }
    return null;
  }

  public static List<String> getVMfiles() {
    if (vmEntries.size() < 1) {
      File[] files = new File(Constants.VM_PATH).listFiles();
      if (files.length > 0) {
        for (String supported : Constants.SUPPORTED_VM)
          for (File file : files)
            if (file.getName().equals(supported)) vmEntries.add(file.getName());
      }
    }
    return vmEntries;
  }
}
