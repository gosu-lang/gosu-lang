/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.expression.IRNumericLiteral;
import gw.lang.ir.expression.IRStringLiteralExpression;

@UnstableAPI
public class IRStringLiteralBuilder extends IRExpressionBuilder {
  private String _value;

  public IRStringLiteralBuilder(String value) {
    _value = value;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRStringLiteralExpression(_value);
  }
}
