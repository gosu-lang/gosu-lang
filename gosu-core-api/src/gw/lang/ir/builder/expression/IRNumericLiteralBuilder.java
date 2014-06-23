/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRNumericLiteral;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRNumericLiteralBuilder extends IRExpressionBuilder {
  private Number _value;

  public IRNumericLiteralBuilder(Number value) {
    _value = value;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRNumericLiteral(_value);
  }
}
