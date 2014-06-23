/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.features;

import gw.lang.PublishedName;

public interface IInvokableFeatureReference<R, T> extends IFeatureReference<R, T>
{
  /**
   * Evaluates reflectively
   */
  public Object evaluate( Object... args );

  /**
   * Returns the method reference as a block in an invokation-friendly syntax
   */
  @PublishedName("invoke")
  T getinvoke();

  /**
   * Returns the method reference as a block in a transformation-friendly syntax
   */
  T toBlock();

  /**
   * @return an array of bound values if the feature had them, and null otherwise
   */
  Object[] getBoundArgValues();
}
