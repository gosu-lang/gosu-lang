/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRStringLiteralExpression extends IRExpression {
  private String _value;

  public IRStringLiteralExpression(String value) {
    _value = value;
  }

  public String getValue() {
    return _value;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.STRING();
  }
}
