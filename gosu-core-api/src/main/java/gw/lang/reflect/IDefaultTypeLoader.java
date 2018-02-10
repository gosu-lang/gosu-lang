/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IModule;

import java.util.Collections;
import java.util.Set;

public interface IDefaultTypeLoader extends ITypeLoader
{
  String JAVA_EXTENSION = "java";
  String DOT_JAVA_EXTENSION = '.' + JAVA_EXTENSION;
  Set<String> EXTENSIONS = Collections.singleton( JAVA_EXTENSION );
  String[] EXTENSIONS_ARRAY = new String[]{DOT_JAVA_EXTENSION};

  Class loadClass( String strFullName );

  IJavaClassInfo getJavaClassInfo( String fullyQualifiedName );

  IJavaClassInfo getJavaClassInfoForClassDirectly( Class clazz, IModule module );

  ISourceFileHandle getSourceFileHandle( String qualifiedName );

  IGosuClassLoader getGosuClassLoader();
}
