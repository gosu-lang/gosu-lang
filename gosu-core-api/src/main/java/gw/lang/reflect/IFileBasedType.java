/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IFile;

// TODO: merge this interface into IType, types that are not file-based should return NO_FILES
public interface IFileBasedType extends IType {
  IFile[] NO_FILES = new IFile[0];

  // TODO: rename this method to something like getResourceFiles() or getOriginalFiles()
  IFile[] getSourceFiles();
}
