/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRClassLiteral;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRClassLiteralBuilder extends IRExpressionBuilder {

  private IRType _literalType;

  public IRClassLiteralBuilder(IRType literalType) {
    _literalType = literalType;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRClassLiteral( _literalType );
  }
}
