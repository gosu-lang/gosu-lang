/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IFile;

public class RefreshRequest {
  public final IFile file;
  public final ITypeLoader typeLoader;
  public final RefreshKind kind;
  public final String[] types;

  public RefreshRequest(IFile file, String[] types, ITypeLoader typeLoader, RefreshKind kind) {
    this.file = file;
    this.kind = kind;
    this.types = types;
    this.typeLoader = typeLoader;
  }

  public RefreshRequest(String[] allTypes, RefreshRequest request, ITypeLoader typeLoader) {
    this(request.file, allTypes, typeLoader, request.kind);
  }

  public RefreshRequest(IFile file, String[] types, RefreshKind kind) {
    this(file, types, getLoader(file), kind);
  }

  private static ITypeLoader getLoader(IFile file) {
    for (ITypeLoader loader : TypeSystem.getModule().getModuleTypeLoader().getTypeLoaderStack()) {
      if (loader.handlesFile(file)) {
        return loader;
      }
    }
    throw new RuntimeException("No type loader for file: " + file);
  }

  @Override
  public String toString() {
    String s = kind + " of ";
    for (String type : types) {
      s += type + ", ";
    }
    s += "from " + (typeLoader != null ? typeLoader : TypeSystem.getModule());
    return s;
  }
}
