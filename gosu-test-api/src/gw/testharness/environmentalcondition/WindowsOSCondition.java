/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testharness.environmentalcondition;

import gw.testharness.IEnvironmentalCondition;

public class WindowsOSCondition implements IEnvironmentalCondition {
  @Override
  public boolean isConditionMet() {
    String osName = System.getProperty("os.name");
    return osName != null && osName.toLowerCase().contains("win");
  }
}
