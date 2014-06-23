/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

public interface IWhereClauseEqualityExpression extends IConditionalExpression, IQueryPartAssembler
{
  boolean isEquals();
}
