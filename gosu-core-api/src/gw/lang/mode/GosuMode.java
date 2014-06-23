/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.mode;

import gw.lang.launch.IArgInfo;

public abstract class GosuMode implements IGosuMode {
  public static final int GOSU_MODE_PRIORITY_HELP = 10;
  public static final int GOSU_MODE_PRIORITY_VERSION = 20;
  public static final int GOSU_MODE_PRIORITY_VERIFY = 60;
  public static final int GOSU_MODE_PRIORITY_INTERACTIVE = 0; // this value is lowest in order for it to be the default (if no args on the command line)
  public static final int GOSU_MODE_PRIORITY_EXECUTE = 90;

  protected IArgInfo _argInfo;

  public GosuMode() {
  }

  @Override
  public final void setArgInfo(IArgInfo argInfo) {
    _argInfo = argInfo;
  }

  @Override
  public final int compareTo(IGosuMode that) {
    return this.getPriority() - that.getPriority();
  }
}
