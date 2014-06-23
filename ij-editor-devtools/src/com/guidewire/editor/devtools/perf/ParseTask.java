/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.perf;

import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;

public class ParseTask implements PerfTask {
//  public static final String NAME = "gw.policy.PolicyPeriodBaseEnhancement";
//  public static final String NAME = "gw.sampledata.forms.BOPSampleFormData";
  public static final String NAME = "gw.sampledata.forms.PASampleFormData";

  IType type;

  public void setup() {
    TypeSystem.pushGlobalModule();
    try {
      type = TypeSystem.getByFullNameIfValid(NAME);
      TypeSystem.refresh((ITypeRef)type);
    } finally {
      TypeSystem.popGlobalModule();
    }
  }

  public void run() {
    TypeSystem.pushGlobalModule();
    try {
      type.isValid();
    } finally {
      TypeSystem.popGlobalModule();
    }
  }

  @Override
  public int getCount() {
    return 50;
  }
}
