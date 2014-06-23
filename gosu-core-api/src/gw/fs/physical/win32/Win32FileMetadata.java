/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical.win32;

import gw.fs.physical.IFileMetadata;
import gw.lang.UnstableAPI;

@UnstableAPI
public class Win32FileMetadata implements IFileMetadata {

  private Win32FindData _findData;

  public Win32FileMetadata(Win32FindData findData) {
    _findData = findData;
  }

  @Override
  public String name() {
    return _findData.getName();
  }

  @Override
  public boolean isDir() {
    return (_findData.getAttributes() & Win32Util.FILE_ATTRIBUTE_DIRECTORY) != 0;
  }

  @Override
  public boolean isFile() {
    return !isDir();
  }

  @Override
  public boolean exists() {
    return true;
  }

  @Override
  public long lastModifiedTime() {
    return _findData.getLastWriteTime();
  }

  @Override
  public long length() {
    return _findData.getSize();
  }
}
