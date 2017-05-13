/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.module.IModule;

import java.util.Set;
import manifold.api.sourceprod.TypeName;

public interface INamespaceType extends INonLoadableType
{
  Set<TypeName> getChildren( IType whosaskin);

  IModule getModule();
}
