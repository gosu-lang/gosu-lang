/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.AbstractPlatformHelper;
import gw.config.ExecutionMode;
import gw.lang.reflect.module.IModule;

public class DefaultPlatformHelper extends AbstractPlatformHelper {
  public static ExecutionMode EXECUTION_MODE = ExecutionMode.RUNTIME;

  @Override
  public ExecutionMode getExecutionMode() {
    return EXECUTION_MODE;
  }

  @Override
  public boolean shouldCacheTypeNames() {
    return false;
  }

  @Override
  public void refresh(IModule module) {
  }
}
