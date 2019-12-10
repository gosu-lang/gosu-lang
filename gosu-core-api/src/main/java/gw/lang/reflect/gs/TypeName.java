/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.init.GosuRuntimeManifoldHost;
import gw.lang.reflect.IType;
import java.util.Objects;
import manifold.api.host.IModule;

//## todo: delete this class and use manifold's TypeName
public class TypeName extends manifold.api.type.TypeName {

  public TypeName(String name, Kind kind, Visibility visibility) {
    super(name, GosuRuntimeManifoldHost.get().getSingleModule(), kind, visibility );
  }
}
