/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.reflect.IType;

public interface IQueryExpressionEvaluator
{
  public Object evaluate();
  public Object evaluate( Object[] ctxArgs );

  public IType getResultType();

  public void addConditionalOrExpression( IWhereClauseConditionalOrExpression whereClauseConditionalOrExpression );

  public void addConditionalAndExpression( IWhereClauseConditionalAndExpression whereClauseConditionalAndExpression );

  public void addEqualityExpression( IWhereClauseEqualityExpression whereClauseEqualityExpression );

  public void addRelationalExpression( IWhereClauseRelationalExpression whereClauseRelationalExpression );

  public void addUnaryExpression( IWhereClauseUnaryExpression whereClauseUnaryExpression );

  public void addQueryPathExpression( IQueryPathExpression queryPathExpression );

  public void addExistsExpression( IWhereClauseExistsExpression whereClauseExistsExpression );

  public void addParenthesizedExpression( IWhereClauseParenthesizedExpression whereClauseParenthesizedExpression );
}
