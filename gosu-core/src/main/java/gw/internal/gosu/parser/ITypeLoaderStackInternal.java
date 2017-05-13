/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import manifold.api.host.RefreshRequest;
import manifold.api.host.RefreshKind;
import gw.lang.reflect.module.ITypeLoaderStack;

import java.util.List;
import manifold.api.fs.IResource;

/**
 */
public interface ITypeLoaderStackInternal extends ITypeLoaderStack {

  List<ITypeLoader> getTypeLoaders();

  void clearErrorTypes();

  INamespaceType getNamespaceType( String strName );

  IType getIntrinsicTypeFromObject( Object object );

  IType getTypeByFullNameIfValid( String fullyQualifiedName, boolean skipJava );

  boolean refresh( IResource file, String typeName, RefreshKind refreshKind);

  void clearFromCaches(RefreshRequest typesToClear);
}
