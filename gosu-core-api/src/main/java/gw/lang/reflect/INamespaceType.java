/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IModule;

import java.util.List;
import java.util.Set;

public interface INamespaceType extends INonLoadableType
{
  Set<TypeName> getChildren(IType whosaskin);

  IModule getModule();
}
