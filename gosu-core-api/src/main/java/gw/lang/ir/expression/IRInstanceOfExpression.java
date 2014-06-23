/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRInstanceOfExpression extends IRExpression {
  private IRExpression _root;
  private IRType _testType;

  public IRInstanceOfExpression(IRExpression root, IRType testType) {
    _root = root;
    _testType = testType;

    root.setParent( this );
  }

  public IRExpression getRoot() {
    return _root;
  }

  public IRType getTestType() {
    return _testType;
  }

  public IRType getType() {
    return IRTypeConstants.pBOOLEAN();
  }


}
