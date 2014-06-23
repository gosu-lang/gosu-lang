/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical.win32;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface INativeWin32APICallback {
  void handleFile(String name, int attributes, long createTime, long lastAccessTime, long lastModifiedTime, long length);
}
