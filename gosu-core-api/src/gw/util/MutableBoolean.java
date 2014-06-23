/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

public class MutableBoolean
{
  private volatile boolean _bValue;

  public boolean isTrue()
  {
    return _bValue;
  }

  public void setValue( boolean bValue )
  {
    _bValue = bValue;
  }
}
