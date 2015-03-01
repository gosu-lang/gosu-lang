/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.lang.UnstableAPI;
import gw.lang.reflect.TypeSystem;

@UnstableAPI
public class TypeSystemLockHelper
{
  public static void getTypeSystemLockWithMonitor( Object objectToLock )
  {
    TypeSystem.lock();
  }
}
