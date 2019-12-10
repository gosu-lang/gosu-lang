/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IDirectory;
import gw.fs.IFile;

import java.util.List;
import java.util.Set;


import static gw.lang.reflect.TypeSystem.getModule;

public abstract class SimpleTypeLoader extends TypeLoaderBase {

  protected SimpleTypeLoader() {
  }

  public abstract Set<String> getExtensions();

  @Override
  public boolean handlesFile(IFile file) {
    return getExtensions().contains(file.getExtension());
  }

  @Override
  public String[] getTypesForFile(IFile file) {
    List<IDirectory> sourcePath = getModule().getSourcePath();
    for (IDirectory src : sourcePath) {
      if (file.isDescendantOf(src)) {
        String fqn = src.relativePath(file);
        fqn = fqn.substring(0, fqn.lastIndexOf('.')).replace('/', '.');
        return new String[] {fqn};
      }
    }
    return new String[0];
  }

  @Override
  public RefreshKind refreshedFile(IFile file, String[] types, RefreshKind kind) {
    return kind;
  }

  @Override
  public boolean handlesDirectory(IDirectory dir) {
    List<IDirectory> sourcePath = getModule().getSourcePath();
    for (IDirectory src : sourcePath) {
      if (dir.isDescendantOf(src)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getNamespaceForDirectory(IDirectory dir) {
    List<IDirectory> sourcePath = getModule().getSourcePath();
    for (IDirectory src : sourcePath) {
      if (dir.isDescendantOf(src)) {
        return src.relativePath(dir).replace('/', '.');
      }
    }
    return null;
  }
}
