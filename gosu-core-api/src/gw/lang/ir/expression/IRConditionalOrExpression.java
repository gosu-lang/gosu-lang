/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRConditionalOrExpression extends IRExpression {
  private IRExpression _lhs;
  private IRExpression _rhs;

  public IRConditionalOrExpression(IRExpression lhs, IRExpression rhs) {
    _lhs = lhs;
    _rhs = rhs;

    _lhs.setParent( this );
    _rhs.setParent( this );
  }

  public IRExpression getLhs() {
    return _lhs;
  }

  public IRExpression getRhs() {
    return _rhs;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.pBOOLEAN();
  }
}
