/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IImplicitTypeAsExpression;

/**
 * An empty subtype of TypeAsExpression to indicate that this is an implicitly done coercion
 */
public class ImplicitTypeAsExpression extends TypeAsExpression implements IImplicitTypeAsExpression
{
  @Override
  public Object evaluate()
  {
    // Note, we override this here so that our Gosu stack integration can map
    // the parsed element (this class) to the one in the stack trace (used to be TypeAsExpression). 
    return super.evaluate();
  }

  @Override
  public String toString()
  {
    return _lhs.toString();
  }


}
