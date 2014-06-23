/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.gs.ICompilableType;

public interface IGosuClassLoadingObserver {

  /**
   * @param gsClass - the class to consider
   * @return true if the class should be loaded in a single serving classloader
   */
  boolean shouldUseSingleServingLoader(ICompilableType gsClass);

}
