/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.ErrorType;
import gw.lang.parser.expressions.INotAWordExpression;
import gw.lang.reflect.IType;

public class NotAWordExpression extends Literal implements INotAWordExpression
{
  public NotAWordExpression() {
  }

  @Override
  public IType getTypeImpl() {
    return ErrorType.getInstance();
  }

  @Override
  public String toString() {
    return "";
  }

  @Override
  public Object evaluate() {
    return null;
  }

}
