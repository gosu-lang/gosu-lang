/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

public interface IIdentityExpression extends IConditionalExpression
{
  boolean isEquals();
}