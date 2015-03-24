/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.config.AbstractPlatformHelper;
import gw.config.ExecutionMode;
import gw.lang.reflect.module.IModule;

public class TestPlatformHelper extends AbstractPlatformHelper {

  @Override
  public ExecutionMode getExecutionMode() {
    return ExecutionMode.IDE;
  }

  @Override
  public boolean shouldCacheTypeNames() {
    return false;
  }

  @Override
  public void refresh(IModule module) {
  }
}
