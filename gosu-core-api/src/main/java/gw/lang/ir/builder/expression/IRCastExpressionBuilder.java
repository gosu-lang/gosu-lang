/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRCastExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRCastExpressionBuilder extends IRExpressionBuilder {

  private IRExpressionBuilder _root;
  private IRType _type;

  public IRCastExpressionBuilder(IRExpressionBuilder root, IRType type) {
    _root = root;
    _type = type;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRCastExpression( _root.build( context ), _type );
  }
}
