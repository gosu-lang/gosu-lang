/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.IRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRNewArrayExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRNewArrayExpressionBuilder extends IRExpressionBuilder {

  private IRType _componentType;
  private IRExpressionBuilder _size;

  public IRNewArrayExpressionBuilder(IRType componentType, IRExpressionBuilder size ) {
    _componentType = componentType;
    _size = size;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRNewArrayExpression(_componentType, _size.build(context) );
  }
}
