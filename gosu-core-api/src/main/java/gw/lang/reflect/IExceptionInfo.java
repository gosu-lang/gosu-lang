/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IExceptionInfo extends IFeatureInfo
{
  /**
   * Returns the intrinsic type this exception represents
   */
  public IType getExceptionType();
}
