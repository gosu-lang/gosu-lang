/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IInitializerExpression;

/**
 */
public class BadInitializerExpression extends Expression implements IInitializerExpression
{
  public Object evaluate()
  {
    throw new UnsupportedOperationException( "evaluate not implemented by gw.internal.gosu.parser.expressions.BadInitializerExpression" );
  }

  @Override
  public String toString()
  {
    return "<Bad Initializer>";
  }

  public void initialize( Object newObject )
  {
    throw new UnsupportedOperationException( "initialize not implemented by gw.internal.gosu.parser.expressions.BadInitializerExpression" );
  }

}