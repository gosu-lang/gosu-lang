/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRMonitorLockAcquireStatement extends IRStatement {
  private IRExpression _monitoredObj;

  public IRMonitorLockAcquireStatement( IRExpression monitoredObject )
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