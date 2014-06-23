/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRArrayLengthExpression extends IRExpression {
  private IRExpression _root;

  public IRArrayLengthExpression(IRExpression root) {
    _root = root;
    root.setParent( this );
  }

  public IRExpression getRoot() {
    return _root;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.pINT();
  }
}
