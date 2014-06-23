/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.reflect.java.asm.IAsmType;

public interface IAsmJavaClassInfo extends IJavaClassInfo {
  IAsmType getAsmType();
}
