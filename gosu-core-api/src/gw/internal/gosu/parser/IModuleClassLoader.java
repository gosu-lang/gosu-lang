/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.UnstableAPI;

/**
 */
@UnstableAPI
public interface IModuleClassLoader {
  /**
   * Load class in this module only, without consulting dependent modules. Note that classloader still can delegate
   * to the parent, if it has any.
   */
  Class<?> loadLocalClass(String name, boolean resolve) throws ClassNotFoundException;

  void dispose();
}
