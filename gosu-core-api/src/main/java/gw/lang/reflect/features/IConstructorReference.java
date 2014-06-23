/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.reflect.IConstructorInfo;

public interface IConstructorReference<R, T> extends IInvokableFeatureReference<R,T> 
{
  /**
   * Returns the method info for this reference
   */
  public IConstructorInfo getConstructorInfo();
}
