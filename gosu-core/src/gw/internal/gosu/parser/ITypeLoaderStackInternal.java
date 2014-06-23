/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.fs.IFile;
import gw.fs.IResource;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.module.ITypeLoaderStack;

import java.util.List;

/**
 */
public interface ITypeLoaderStackInternal extends ITypeLoaderStack {

  List<ITypeLoader> getTypeLoaders();

  void clearErrorTypes();

  INamespaceType getNamespaceType( String strName );

  IType getIntrinsicTypeFromObject( Object object );

  IType getTypeByFullNameIfValid( String fullyQualifiedName, boolean skipJava );

  boolean refresh(IResource file, String typeName, RefreshKind refreshKind);

  void clearFromCaches(RefreshRequest typesToClear);
}
