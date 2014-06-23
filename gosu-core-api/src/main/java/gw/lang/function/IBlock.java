/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.gs.IGosuClassObject;
import gw.lang.parser.expressions.IBlockExpression;

public interface IBlock extends IGosuClassObject
{
  public static final int MAX_ARGS = 16;

  public Object invokeWithArgs( Object... args );

  public IBlockExpression getParsedElement();

  IFunctionType getFunctionType();
}
