/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRNegationExpression extends IRExpression {

  private IRExpression _root;

  public IRNegationExpression(IRExpression root) {
    _root = root;
    setParentToThis( root );
  }

  public IRExpression getRoot() {
    return _root;
  }

  @Override
  public IRType getType() {
    return _root.getType();
  }
}
