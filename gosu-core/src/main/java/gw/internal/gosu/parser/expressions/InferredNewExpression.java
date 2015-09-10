/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IInferredNewExpression;

/**
 */
public class InferredNewExpression extends NewExpression implements IInferredNewExpression
{
  @Override
  public boolean isCompileTimeConstant()
  {
    if( getInitializer() == null )
    {
      return true;
    }
    return super.isCompileTimeConstant();
  }
}