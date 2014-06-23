/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

public class CommandFailedException extends RuntimeException
{
  private int _errorCode;

  public CommandFailedException( int i, String msg )
  {
    super( msg );
    _errorCode = i;
  }

  public int getErrorCode()
  {
    return _errorCode;
  }
  
}
