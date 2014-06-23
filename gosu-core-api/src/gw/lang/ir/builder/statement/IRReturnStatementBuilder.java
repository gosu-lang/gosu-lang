/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRStatementBuilder;
import gw.lang.ir.statement.IRReturnStatement;

@UnstableAPI
public class IRReturnStatementBuilder extends IRStatementBuilder {

  private IRExpressionBuilder _value;

  public IRReturnStatementBuilder() {
  }

  public IRReturnStatementBuilder(IRExpressionBuilder value) {
    _value = value;
  }

  @Override
  protected IRStatement buildImpl(IRBuilderContext context) {
    IRExpression value;
    if (_value == null) {
      value = null;
    } else {
      value = IRArgConverter.castOrConvertIfNecessary( context.currentReturnType(), _value.build( context ) );
    }
    return new IRReturnStatement(null, value);
  }
}
