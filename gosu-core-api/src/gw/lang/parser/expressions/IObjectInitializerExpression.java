/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import java.util.List;

public interface IObjectInitializerExpression extends IInitializerExpression
{
  public List<? extends IInitializerAssignment> getInitializers();
}
