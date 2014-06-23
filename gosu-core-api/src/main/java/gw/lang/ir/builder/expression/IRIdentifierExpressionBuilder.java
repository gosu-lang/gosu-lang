/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRSymbolBuilder;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRIdentifierExpressionBuilder extends IRExpressionBuilder {

  private IRSymbolBuilder _symbol;

  public IRIdentifierExpressionBuilder(IRSymbolBuilder symbol) {
    _symbol = symbol;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRIdentifier(_symbol.build(context));
  }
}
