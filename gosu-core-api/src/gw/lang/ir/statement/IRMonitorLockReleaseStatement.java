/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRMonitorLockReleaseStatement extends IRStatement {
  private IRExpression _monitoredObj;

  public IRMonitorLockReleaseStatement( IRExpression monitoredObject )
  {
    _monitoredObj = monitoredObject;
  }

  public IRExpression getMonitoredObject()
  {
    return _monitoredObj;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement()
  {
    return null;
  }
}