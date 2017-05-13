/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import manifold.api.fs.IDirectory;

public interface INativeModule {
  Object getNativeModule();
  IDirectory getOutputPath();
}
