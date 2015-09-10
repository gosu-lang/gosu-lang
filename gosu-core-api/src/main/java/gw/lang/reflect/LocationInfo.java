/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.net.URL;

/**
 * Representation of a particular location in a particular source file.
 */
public class LocationInfo implements ILocationInfo {

  private final int _line;
  private final int _column;
  private final int _offset;
  private final int _textLength;
  private final URL _fileUrl;

  public LocationInfo( int line, int column, URL fileUrl ) {
    this( -1, -1, line, column, fileUrl );
  }

  public LocationInfo( int offset, int textLength, int line, int column, URL fileUrl ) {
    _offset = offset;
    _textLength = textLength;
    _line = line;
    _column = column;
    _fileUrl = fileUrl;
  }

  @Override
  public boolean hasLocation()
  {
    return true;
  }

  public int getOffset()
  {
    return _offset;
  }

  public int getTextLength() {
    return _textLength;
  }

  public int getLine() {
    return _line;
  }

  public int getColumn() {
    return _column;
  }

  public URL getFileUrl() {
    return _fileUrl;
  }

}
