/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.reflect.IHasJavaClass;

public interface IJavaBackedTypeData extends IHasJavaClass {
  IJavaClassInfo getBackingClassInfo();
}
