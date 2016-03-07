/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;

@UnstableAPI
public abstract class IRExpression extends IRElement {
  private ConditionContext _condContext;

  public IRExpression()
  {
    _condContext = new ConditionContext();
  }

  public abstract IRType getType();

  public ConditionContext getConditionContext()
  {
    return _condContext;
  }
}
