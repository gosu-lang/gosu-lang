/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.expressions.Literal;

/**
 */
public class DefaultParamValueLiteral extends Literal
{
  private static DefaultParamValueLiteral INSTANCE = null;

  public static DefaultParamValueLiteral instance()
  {
    if( INSTANCE == null )
    {
      INSTANCE = new DefaultParamValueLiteral();
    }
    return INSTANCE;
  }

  private DefaultParamValueLiteral() {
    setType(ErrorType.getInstance());
  }

  @Override
  public String toString()
  {
    return "Default Param Value Expression: This is invalid";
  }

}
