/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRNumericLiteral extends IRExpression {
  private Number _value;

  public IRNumericLiteral(Number value) {
    _value = value;
  }

  public Number getValue() {
    return _value;
  }

  @Override
  public IRType getType() {
    if (_value instanceof Byte) {
      return IRTypeConstants.pBYTE();
    } else if (_value instanceof Short) {
      return IRTypeConstants.pSHORT();
    } else if (_value instanceof Integer) {
      return IRTypeConstants.pINT();
    } else if (_value instanceof Long) {
      return IRTypeConstants.pLONG();
    } else if (_value instanceof Float) {
      return IRTypeConstants.pFLOAT();
    } else if (_value instanceof Double) {
      return IRTypeConstants.pDOUBLE();
    } else {
      throw new IllegalStateException("Unexpected value " + _value.getClass());
    }
  }
}
