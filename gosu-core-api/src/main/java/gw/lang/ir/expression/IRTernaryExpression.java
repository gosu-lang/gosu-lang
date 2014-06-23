/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRTernaryExpression extends IRExpression {
  private IRExpression _test;
  private IRExpression _trueValue;
  private IRExpression _falseValue;
  private IRType _resultType;

  public IRTernaryExpression(IRExpression test, IRExpression trueValue, IRExpression falseValue, IRType resultType) {
    _test = test;
    _trueValue = trueValue;
    _falseValue = falseValue;
    _resultType = resultType;

    test.setParent( this );
    trueValue.setParent( this );
    falseValue.setParent( this );
  }

  public IRExpression getTest() {
    return _test;
  }

  public IRExpression getTrueValue() {
    return _trueValue;
  }

  public IRExpression getFalseValue() {
    return _falseValue;
  }

  public IRType getResultType() {
    return _resultType;
  }

  @Override
  public IRType getType() {
    return _resultType;
  }
}
