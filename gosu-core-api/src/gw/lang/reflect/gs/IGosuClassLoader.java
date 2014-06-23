/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.reflect.java.IJavaType;


public interface IGosuClassLoader
{
  Class<?> findClass( String strName ) throws ClassNotFoundException;

  IJavaType getFunctionClassForArity(int length);

  void dumpAllClasses();

  Class loadClass(String className) throws ClassNotFoundException;

  ClassLoader getActualLoader();

  Class defineClass( String name, byte[] bytes );

  byte[] getBytes( ICompilableType gsClass );

  void assignParent( ClassLoader classLoader );
}
