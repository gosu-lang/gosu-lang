/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeRefFactory;
import gw.lang.UnstableAPI;

import java.util.List;
import java.util.Set;
import manifold.api.sourceprod.TypeName;

@UnstableAPI
public interface ITypeLoaderStack
{
  List<ITypeLoader> getTypeLoaderStack();

  ITypeRefFactory getTypeRefFactory();

  IModule getModule();
  
  <T extends ITypeLoader> T getTypeLoader( Class<? extends T> loaderType );

  IDefaultTypeLoader getDefaultTypeLoader();

  void refreshed();

  void shutdown();

  Set<TypeName> getTypeNames( String namespace);
}
