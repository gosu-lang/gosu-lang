/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.Collections;
import java.util.Set;

public interface IDefaultTypeLoader extends ITypeLoader
{
  public static final String DOT_JAVA_EXTENSION = ".java";
  public static final String JAVA_EXTENSION = "java";
  public static final Set<String> EXTENSIONS = Collections.singleton("java");
  public static final String[] EXTENSIONS_ARRAY = new String[] {DOT_JAVA_EXTENSION};

  Class loadClass(String strFullName);

  IJavaClassInfo getJavaClassInfo(String fullyQualifiedName);

  IJavaClassInfo getJavaClassInfoForClassDirectly( Class clazz );

  /**
   * @deprecated use getSourceFileHandle(String) instead 
   */
  default ISourceFileHandle getSouceFileHandle(String qualifiedName) {
    return getSourceFileHandle(qualifiedName);
  }
  
  ISourceFileHandle getSourceFileHandle(String qualifiedName);

  IGosuClassLoader getGosuClassLoader();
}
