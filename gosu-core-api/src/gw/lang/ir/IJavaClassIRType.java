/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;
import gw.lang.reflect.java.IJavaClassInfo;

@UnstableAPI
public interface IJavaClassIRType extends IRType {
  IJavaClassInfo getJavaClassInfo();
}
