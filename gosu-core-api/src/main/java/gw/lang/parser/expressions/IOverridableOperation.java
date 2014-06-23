/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.reflect.IMethodInfo;

public interface IOverridableOperation
{
  IMethodInfo getOverride();
  void setOverride( IMethodInfo overrideMi );
}
