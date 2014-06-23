/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.statement;

import gw.lang.ir.builder.IRStatementBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRArgConverter;
import gw.lang.ir.IRType;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.statement.IRArrayStoreStatement;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRArrayStoreStatementBuilder extends IRStatementBuilder {

  private IRExpressionBuilder _target;
  private IRExpressionBuilder _index;
  private IRExpressionBuilder _value;

  public IRArrayStoreStatementBuilder(IRExpressionBuilder target, IRExpressionBuilder index, IRExpressionBuilder value) {
    _target = target;
    _index = index;
    _value = value;
  }

  @Override
  protected IRStatement buildImpl(IRBuilderContext context) {
    IRExpression target = _target.build( context );
    IRType componentType = target.getType().getComponentType();
    IRExpression value = IRArgConverter.castOrConvertIfNecessary(componentType, _value.build( context ) );
    return new IRArrayStoreStatement( target, _index.build( context ), value, componentType );
  }
}
