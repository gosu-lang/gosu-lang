/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.perf.codestats;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class ProjectInfo {
  private File projectFolder;
  private Map<String, FileInfo> infos = new HashMap<String, FileInfo>();

  public ProjectInfo(String project, String[] extensions) {
    projectFolder = new File(project);
    for (String extension : extensions) {
      infos.put(extension, new FileInfo(extension));
    }
  }

  private void processFile(File fileOrFolder) {
    File[] files = fileOrFolder.listFiles();
    for (File file : files) {
      String name = file.getName();
      if (file.isDirectory()) {
        processFile(file);
      } else {
        try {
          int beginIndex = name.lastIndexOf('.');
          if (beginIndex > 0) {
            String extension = name.substring(beginIndex);
            FileInfo fileInfo = infos.get(extension);
            if (fileInfo != null) {
              fileInfo.process(file);
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void printInfo() {
    processFile(projectFolder);

    System.out.println("Stats for " + projectFolder);
    System.out.println("===================================");
    FileInfo totals = new FileInfo(" total");
    for (String extension : infos.keySet()) {
      FileInfo fileInfo = infos.get(extension);
      fileInfo.printInfo();
      totals.add(fileInfo);
    }
    totals.printInfo();
    System.out.println();
  }

  public static void main(String[] args) {
    String[] extensions = {".java", ".gs", ".gsx"};
    new ProjectInfo("C:\\dev\\emerald\\studio\\platform\\ij-studio\\src", extensions).printInfo();
  }
}
