/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IExpressionRuntime;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

public interface IMemberAccessExpression extends IExpression
{
  IExpression getRootExpression();

  IType getRootType();
  
  String getMemberName();

  int getStartOffset();
  void setStartOffset( int iOffset );

  void setExpressionRuntime(IExpressionRuntime expressionRuntime);

  IPropertyInfo getPropertyInfo();

  IExpressionRuntime getExpressionRuntime();
}
