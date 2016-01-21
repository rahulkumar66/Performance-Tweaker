package com.rattlehead666.performancetweaker.app.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class VmUtils {

  //private static List<String> vmEntries = new ArrayList<>();
  private static LinkedHashMap<String, String> vmEntries = new LinkedHashMap<>();

  public static void setVM(final String value, final String name) {
    SysUtils.executeRootCommand(new ArrayList<String>() {{
      add("echo " + value + " > " + Constants.VM_PATH+"/"+name + "\n");
      add("exit" + "\n");
    }});
  }

  public static String getVMValue(String file) {
    if (new File(Constants.VM_PATH + "/" + file).exists()) {
      String value = SysUtils.readOutputFromFile(Constants.VM_PATH + "/" + file);
      if (value != null) return value;
    }
    return null;
  }

  public static LinkedHashMap<String, String> getVMfiles() {
    if (vmEntries.size() < 1) {
      File[] files = new File(Constants.VM_PATH).listFiles();
      if (files.length > 0) {
        for (String supported : Constants.SUPPORTED_VM)
          for (File file : files)
            if (file.getName().equals(supported)) {
              vmEntries.put(file.getName(), getVMValue(file.getName()));
            }
      }
    }
    return vmEntries;
  }
}
