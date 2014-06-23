/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testharness.environmentalcondition;

import gw.testharness.IEnvironmentalCondition;

public class IBMJVMCondition implements IEnvironmentalCondition {

  @Override
  public boolean isConditionMet() {
    String vmName = System.getProperty("java.vm.name");
    return vmName != null && vmName.toLowerCase().contains("ibm");
  }
}
