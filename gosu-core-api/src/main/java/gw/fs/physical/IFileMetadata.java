/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IFileMetadata {
  String name();
  boolean isDir();
  boolean isFile();
  boolean exists();
  long lastModifiedTime();
  long length();
}
