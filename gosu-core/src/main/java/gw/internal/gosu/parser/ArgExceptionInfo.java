/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IToken;
import gw.lang.reflect.IType;
import gw.lang.parser.exceptions.ParseException;

/**
 */
final class ArgExceptionInfo
{
  private int _iArgIndex;
  private ParseException _pe;
  private IToken _parserState;
  private IType _expectedType;
  private IType _expectedQualifierType;

  public ArgExceptionInfo( int iArgIndex, ParseException pe, IToken parserState,
                           IType expectedType, IType expectedQualifierType )
  {
    _iArgIndex = iArgIndex;
    _pe = pe;
    _parserState = parserState;
    _expectedType = expectedType;
    _expectedQualifierType = expectedQualifierType;
  }

  public int getArgIndex()
  {
    return _iArgIndex;
  }

  public ParseException getPe()
  {
    return _pe;
  }

  public IToken getParserState()
  {
    return _parserState;
  }

  public IType getExpectedType()
  {
    return _expectedType;
  }

  public IType getExpectedQualifierType()
  {
    return _expectedQualifierType;
  }
}
