/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs;

import gw.lang.UnstableAPI;

@UnstableAPI
public class IFileUtil {

  public static String getExtension(IFile file) {
    int lastDot = file.getName().lastIndexOf(".");
    if (lastDot >= 0) {
      return file.getName().substring(lastDot + 1);
    }
    else {
      return "";
    }
  }

  public static String getExtension(String fileName) {
    int lastDot = fileName.lastIndexOf(".");
    if (lastDot >= 0) {
      return fileName.substring(lastDot + 1);
    }
    else {
      return "";
    }
  }

  public static String getBaseName(IFile file) {
    return getBaseName( file.getName() );
  }

  public static String getBaseName(String fileName) {
    int lastDot = fileName.lastIndexOf(".");
    if (lastDot >= 0) {
      return fileName.substring(0, lastDot);
    }
    else {
      return "";
    }
  }
}
