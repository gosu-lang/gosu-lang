/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.reflect.IType;

public interface IQueryExpression extends IExpression, IParsedElementWithAtLeastOneDeclaration
{
  IType getEntityType();

  String getIdentifier();

  IQueryPathExpression getInExpression();

  IExpression getWhereClauseExpression();

  void buildPrimaryQuery( IQueryExpressionEvaluator evaluator );

  boolean isDistinct();

  Object evalRhsExpr( IExpression rhs, Object[] ctxArgs );
}
