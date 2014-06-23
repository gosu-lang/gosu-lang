/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRStatementBuilder;
import gw.lang.ir.builder.IRSymbolBuilder;
import gw.lang.ir.statement.IRAssignmentStatement;

@UnstableAPI
public class IRAssignmentStatementBuilder extends IRStatementBuilder {

  private IRSymbolBuilder _root;
  private IRExpressionBuilder _value;

  public IRAssignmentStatementBuilder(IRSymbolBuilder root, IRExpressionBuilder value) {
    _root = root;
    _value = value;
  }

  @Override
  protected IRStatement buildImpl(IRBuilderContext context) {
    IRSymbol root = _root.build(context);
    IRExpression value = IRArgConverter.castOrConvertIfNecessary( root.getType(), _value.build( context ) );
    return new IRAssignmentStatement( root, value );
  }
}
