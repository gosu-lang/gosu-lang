/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.expression;

import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRStatementBuilder;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRElement;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.UnstableAPI;

import java.util.List;
import java.util.ArrayList;

@UnstableAPI
public class IRCompositeExpressionBuilder extends IRExpressionBuilder {

  private List<IRStatementBuilder> _statements;
  private IRExpressionBuilder _finalExpression;

  public IRCompositeExpressionBuilder(List<IRStatementBuilder> statements, IRExpressionBuilder finalExpression) {
    _statements = statements;
    _finalExpression = finalExpression;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    List<IRElement> elements = new ArrayList<IRElement>();
    for (IRStatementBuilder statement : _statements) {
      elements.add(statement.build(context));
    }
    elements.add(_finalExpression.build(context));

    return new IRCompositeExpression(elements);
  }
}
