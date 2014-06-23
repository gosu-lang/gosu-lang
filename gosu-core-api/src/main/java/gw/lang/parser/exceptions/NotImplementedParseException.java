/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.resources.Res;
import gw.lang.parser.IFullParserState;
import gw.lang.reflect.IFunctionType;

public class NotImplementedParseException extends ParseException
{
  private IGosuClass _gsClass;
  private IFunctionType _funcType;

  public NotImplementedParseException( IFullParserState parserState, IGosuClass gsClass, String strClass, IFunctionType funcType )
  {
    super( parserState, Res.MSG_UNIMPLEMENTED_METHOD, strClass, funcType );
    _gsClass = gsClass;
    _funcType = funcType;
  }

  public IGosuClass getGsClass()
  {
    return _gsClass;
  }

  public IFunctionType getFuncType()
  {
    return _funcType;
  }
}
