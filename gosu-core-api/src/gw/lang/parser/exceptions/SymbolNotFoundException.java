/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.Res;
import gw.lang.parser.IFullParserState;

public class SymbolNotFoundException extends ParseException
{
  private String _symbolName;

  public SymbolNotFoundException( IFullParserState parserState, String symbolName )
  {
    super( parserState, Res.MSG_BAD_IDENTIFIER_NAME, symbolName );
    _symbolName = symbolName;
  }

  public String getSymbolName()
  {
    return _symbolName;
  }

}
