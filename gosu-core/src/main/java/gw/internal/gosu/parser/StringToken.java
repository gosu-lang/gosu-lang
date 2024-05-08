/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IToken;
import gw.lang.parser.SourceCodeReader;
import gw.util.cache.StringPool;

/**
*/
public class StringToken extends Token
{
  String _strStringLiteralValue;

  public StringToken()
  {
    super();
  }

  @Override
  public IToken copy()
  {
    StringToken copy = (StringToken)super.copy();
    copy._strStringLiteralValue = _strStringLiteralValue;
    return copy;
  }

  @Override
  Token create()
  {
    return new StringToken();
  }

  protected void assignContent( String strValue, SourceCodeReader document )
  {
      _strValue = StringPool.get(strValue);
    _strStringLiteralValue = getMyTextFromSource( document );
  }

  public String getText()
  {
    return _strStringLiteralValue;
  }
}
