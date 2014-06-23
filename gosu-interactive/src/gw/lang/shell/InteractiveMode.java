/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.shell;

import gw.lang.Gosu;
import gw.lang.mode.GosuMode;
import gw.lang.mode.RequiresInit;

@RequiresInit
public class InteractiveMode extends GosuMode {

  @Override
  public int getPriority() {
    return GOSU_MODE_PRIORITY_INTERACTIVE;
  }

  @Override
  public boolean accept() {
    return _argInfo.consumeArg(Gosu.ARGKEY_INTERACTIVE);
  }

  @Override
  public int run() {
    new InteractiveShell(true).run();
    return 0;
  }
}
