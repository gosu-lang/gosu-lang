/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRNotExpression extends IRExpression {

  private IRExpression _root;

  public IRNotExpression(IRExpression root) {
    _root = root;

    root.setParent( this );
  }

  public IRExpression getRoot() {
    return _root;
  }

  @Override
  public IRType getType() {
    return _root.getType();
  }
}
