/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.gs.IGosuClassObject;
import gw.lang.parser.expressions.IBlockExpression;

public interface IBlock extends IGosuClassObject
{
  int MAX_ARGS = 16;

  Object invokeWithArgs( Object... args );

  IBlockExpression getParsedElement();

  IFunctionType getFunctionType();
}
