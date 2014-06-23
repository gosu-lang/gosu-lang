/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.expressions.ICharLiteralExpression;

/**
 * Represents a String literal expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class CharLiteral extends Literal implements ICharLiteralExpression
{
  protected Character _char;

  public CharLiteral( char c )
  {
    _char = c;
    setType( GosuParserTypes.CHAR_TYPE() );
  }

  public Character getValue()
  {
    return _char;
  }

  public boolean isCompileTimeConstant()
  {
    return true;
  }

  public Object evaluate()
  {
    return _char;
  }

  @Override
  public String toString()
  {
    return "\'" + escapeQuotes() + "\'";
  }

  private String escapeQuotes()
  {
    if( _char == '\'' )
    {
      return "\\\'";
    }
    return _char.toString();
  }

}