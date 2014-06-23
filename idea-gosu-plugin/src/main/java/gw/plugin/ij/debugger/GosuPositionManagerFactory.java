/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.debugger;

import com.intellij.debugger.PositionManager;
import com.intellij.debugger.PositionManagerFactory;
import com.intellij.debugger.engine.DebugProcess;

public class GosuPositionManagerFactory extends PositionManagerFactory {
  @Override
  public PositionManager createPositionManager(DebugProcess process) {
    return new GosuPositionManager(process);
  }
}
