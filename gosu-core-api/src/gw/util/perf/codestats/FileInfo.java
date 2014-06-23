/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.perf.codestats;

import gw.util.StreamUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileInfo {
  public String extension;
  public int fileCount;
  public int totalLines;
  public int codeLines;

  public FileInfo(String extension) {
    this.extension = extension;
  }

  public void process(File file) throws IOException {
    fileCount ++;

    BufferedReader reader = new BufferedReader(StreamUtil.getInputStreamReader(new FileInputStream(file)));
    try {
      for (String s = reader.readLine(); s != null; s = reader.readLine()) {
        totalLines++;
        s = s.trim();
        if (s.length() != 0 && !s.equals('{') && !s.equals('}')) {
          codeLines++;
        }
      }
    } finally {
      StreamUtil.close(reader);
    }
  }

  public void printInfo() {
    System.out.println(fileCount + " " + extension.substring(1).toUpperCase() + " files: " + totalLines + "/" + codeLines + " lines");
  }

  public void add(FileInfo fileInfo) {
    fileCount += fileInfo.fileCount;
    totalLines += fileInfo.totalLines;
    codeLines += fileInfo.codeLines;
  }
}
