/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRNullLiteralBuilder extends IRExpressionBuilder {
  public IRNullLiteralBuilder() {
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRNullLiteral();
  }
}
