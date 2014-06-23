/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRStatement;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRStatementBuilder;
import gw.lang.ir.statement.IRIfStatement;

@UnstableAPI
public class IRIfStatementBuilder extends IRStatementBuilder {

  private IRExpressionBuilder _test;
  private IRStatementBuilder _ifStatement;
  private IRStatementBuilder _elseStatement;

  public IRIfStatementBuilder(IRExpressionBuilder test) {
    _test = test;
  }

  public IRIfStatementBuilder then(IRStatementBuilder ifStatement) {
    _ifStatement = ifStatement;
    return this;
  }

  public IRStatementBuilder _else(IRStatementBuilder elseStatement) {
    _elseStatement = elseStatement;
    return this;
  }

  @Override
  protected IRStatement buildImpl(IRBuilderContext context) {
    return new IRIfStatement(_test.build(context), (_ifStatement == null ? null : _ifStatement.build(context)), (_elseStatement == null ? null : _elseStatement.build(context)));
  }
}
