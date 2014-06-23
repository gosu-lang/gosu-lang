/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.net.URL;

/**
 * Representation of a particular location in a particular source file.
 */
public class LocationInfo {

  private final int _lineNumber;
  private final int _columnNumber;
  private final URL _fileUrl;

  public LocationInfo( int lineNumber, int columnNumber, URL fileUrl ) {
    _lineNumber = lineNumber;
    _columnNumber = columnNumber;
    _fileUrl = fileUrl;
  }

  public int getLineNumber() {
    return _lineNumber;
  }

  public int getColumnNumber() {
    return _columnNumber;
  }

  public URL getFileUrl() {
    return _fileUrl;
  }

}
