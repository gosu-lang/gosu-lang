/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

public class ErrantGosuClassException extends RuntimeException
{
  private IGosuClass _gsClass;

  public ErrantGosuClassException( IGosuClass gsClass )
  {
    _gsClass = gsClass;
  }

  @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
  public String getMessage()
  {
    ParseResultsException resultsException = _gsClass.getParseResultsException();
    if( resultsException == null )
    {
      TypeSystem.lock();
      try
      {
        _gsClass.isValid();
        resultsException = _gsClass.getParseResultsException();
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    String strResultsMsg = resultsException == null ? "" : resultsException.getMessage();
    return "GosuClass " + _gsClass.getName() +
           " has errors, and cannot be used at runtime.\n"
           + strResultsMsg;
  }

  public IGosuClass getGsClass()
  {
    return _gsClass;
  }
}