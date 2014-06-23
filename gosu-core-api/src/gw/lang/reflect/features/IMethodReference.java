/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.reflect.IMethodInfo;

public interface IMethodReference<R, T> extends IInvokableFeatureReference<R, T>
{
  /**
   * Returns the method info for this reference
   */
  public IMethodInfo getMethodInfo();
}
