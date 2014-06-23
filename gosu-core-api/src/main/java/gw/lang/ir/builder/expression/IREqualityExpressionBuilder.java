/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.expression.IREqualityExpression;

@UnstableAPI
public class IREqualityExpressionBuilder extends IRExpressionBuilder {

  private IRExpressionBuilder _lhs;
  private IRExpressionBuilder _rhs;
  private boolean _equals;

  public IREqualityExpressionBuilder(IRExpressionBuilder lhs, IRExpressionBuilder rhs, boolean equals) {
    _lhs = lhs;
    _rhs = rhs;
    _equals = equals;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IREqualityExpression(_lhs.build(context), _rhs.build(context), _equals);
  }
}
