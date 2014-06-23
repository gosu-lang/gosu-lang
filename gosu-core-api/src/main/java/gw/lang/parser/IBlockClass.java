/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

public interface IBlockClass extends IGosuClass
{
  public static final String INVOKE_METHOD_NAME = "invoke";
  public static final String INVOKE_WITH_ARGS_METHOD_NAME = "invokeWithArgs";

  public IBlockExpression getBlock();

  IType getBlockType();
}
