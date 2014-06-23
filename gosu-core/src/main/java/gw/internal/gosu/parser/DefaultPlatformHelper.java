/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.AbstractPlatformHelper;
import gw.lang.reflect.module.IModule;

public class DefaultPlatformHelper extends AbstractPlatformHelper {

  public static boolean IN_IDE = false;

  @Override
  public boolean isInIDE() {
    return IN_IDE;
  }

  @Override
  public boolean shouldCacheTypeNames() {
    return false;
  }

  @Override
  public void refresh(IModule module) {
  }
}
