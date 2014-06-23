/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IHasArguments;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IFeatureInfo;


import java.util.Stack;

public interface IBeanMethodCallExpression extends IMemberAccessExpression, IHasArguments
{
  IFunctionType getFunctionType();

  IType[] getArgTypes();

  IExpression[] getArgs();

  IMethodInfo getMethodDescriptor();

  IMethodInfo getGenericMethodDescriptor();

}
