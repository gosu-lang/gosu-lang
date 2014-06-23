/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical.win32;

import gw.lang.UnstableAPI;

import java.util.Date;

@UnstableAPI
public class Win32FindData {
  private final int _attributes;
  private final long _creationTime;
  private final long _lastAccessTime;
  private final long _lastWriteTime;
  private final long _size;
  private final String _name;

  public Win32FindData(int attributes, long creationTime, long lastAccessTime, long lastWriteTime, long size, String name) {
    _attributes = attributes;
    _creationTime = creationTime;
    _lastAccessTime = lastAccessTime;
    _lastWriteTime = lastWriteTime;
    _size = size;
    _name = name;
  }

  public int getAttributes() {
    return _attributes;
  }

  public long getCreationTime() {
    return _creationTime;
  }

  public long getLastAccessTime() {
    return _lastAccessTime;
  }

  public long getLastWriteTime() {
    return _lastWriteTime;
  }

  public long getSize() {
    return _size;
  }

  public String getName() {
    return _name;
  }

  public String toDebugString() {
    StringBuilder sb = new StringBuilder();
    sb.append("name: ").append(_name).append("\n");
    sb.append("attributes: ").append(_attributes).append("\n");
    sb.append("creation time: ").append(new Date(_creationTime)).append("\n");
    sb.append("last access time: ").append(new Date(_lastAccessTime)).append("\n");
    sb.append("last write time: ").append(new Date(_lastWriteTime)).append("\n");
    sb.append("size: ").append(_size);
    return sb.toString();
  }
}
