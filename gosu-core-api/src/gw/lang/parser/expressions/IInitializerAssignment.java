/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;
import gw.lang.reflect.IPropertyInfo;

public interface IInitializerAssignment extends IStatement
{
  public String getPropertyName();
  public IPropertyInfo getPropertyInfo();
  public IExpression getRhs();
}
