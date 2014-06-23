/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

import java.util.List;

public interface ICollectionInitializerExpression extends IInitializerExpression {
  public List<IExpression> getValues();
}
