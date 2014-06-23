/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.java;

import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

import java.lang.reflect.Method;

public class JavaAnnotationUtil
{
  public static Method getMethod( IGosuClass gsClass, String s )
  {
    Class<?> backingClass = gsClass.getBackingClass();
    for( Method method : TypeSystem.getDeclaredMethods( backingClass ) )
    {
      if( method.getName().equals( s ) )
      {
        return method;
      }
    }
    return null;
  }
}
