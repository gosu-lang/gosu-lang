/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import java.util.List;
import manifold.api.fs.IDirectory;

public interface INativeModule {
  Object getNativeModule();
  List<IDirectory> getOutputPath();
}
