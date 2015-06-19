/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.StringCache;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.expressions.IStringLiteralExpression;

/**
 * Represents a String literal expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public class StringLiteral extends Literal implements IStringLiteralExpression
{
  protected String _strValue;

  public StringLiteral( String strValue )
  {
    _strValue = StringCache.get(strValue);
    setType( GosuParserTypes.STRING_TYPE() );
  }

  public String getValue()
  {
    return _strValue;
  }

  public boolean isCompileTimeConstant()
  {
    return true;
  }

  public Object evaluate()
  {
    return getValue();
  }

  @Override
  public String toString()
  {
    return "\"" + escapeQuotes() + "\"";
  }

  private String escapeQuotes()
  {
    String strValue = getValue();
    if( strValue != null )
    {
      strValue = strValue.replace( "\"", "\\\"" );
    }
    return strValue;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    StringLiteral that = (StringLiteral)o;

    return !(_strValue != null ? !_strValue.equals( that._strValue ) : that._strValue != null);
  }

  @Override
  public int hashCode()
  {
    return _strValue != null ? _strValue.hashCode() : 0;
  }
}
