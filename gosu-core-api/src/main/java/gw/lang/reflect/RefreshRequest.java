/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IFile;
import gw.lang.reflect.module.IModule;
import gw.util.IdentitySet;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class RefreshRequest {
  public final IFile file;
  public final IModule module;
  public final ITypeLoader typeLoader;
  public final RefreshKind kind;
  public final String[] types;

  public RefreshRequest(IFile file, String[] types, IModule module, ITypeLoader typeLoader, RefreshKind kind) {
    this.file = file;
    this.kind = kind;
    this.types = types;
    this.module = module;
    this.typeLoader = typeLoader;
  }

  public RefreshRequest(IFile file, String[] types, ITypeLoader typeLoader, RefreshKind kind) {
    this(file, types, getModule(typeLoader), typeLoader, kind);
  }

  public RefreshRequest(String[] allTypes, RefreshRequest request, ITypeLoader typeLoader) {
    this(request.file, allTypes, typeLoader, request.kind);
  }

  public RefreshRequest(IFile file, String[] types, IModule module, RefreshKind kind) {
    this(file, types, getLoader(file, module), kind);
  }

  private static ITypeLoader getLoader(IFile file, IModule module) {
    for (ITypeLoader loader : module.getModuleTypeLoader().getTypeLoaderStack()) {
      if (loader.handlesFile(file)) {
        return loader;
      }
    }
    throw new RuntimeException("No type loader for file: " + file);
  }

  private static IModule getModule(ITypeLoader typeLoader) {
    if (typeLoader == null) {
      throw new RuntimeException("A refresh request must have a valid typeloader");
    }
    return typeLoader.getModule();
  }

  @Override
  public String toString() {
    String s = kind + " of ";
    for (String type : types) {
      s += type + ", ";
    }
    s += "from " + (typeLoader != null ? typeLoader : module);
    return s;
  }
}
