/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.ISource;
import java.util.Collections;
import java.util.Set;
import manifold.api.fs.IFile;
import manifold.api.type.ClassType;
import manifold.api.type.ITypeManifold;

public interface ISourceFileHandle
{
  ISource getSource();

  String getParentType();

  String getNamespace();

  String getFilePath();

  boolean isTestClass();

  boolean isValid();

  boolean isStandardPath();
  boolean isIncludeModulePath();

  void cleanAfterCompile();

  ClassType getClassType();

  String getTypeNamespace();

  String getRelativeName();

  void setOffset( int iOffset );
  int getOffset();
  void setEnd( int iEnd );
  int getEnd();

  /**
   * @return a relative representation of the file appropriate for debugging/stack traces
   */
  String getFileName();

  IFile getFile();

  default Set<ITypeManifold> getTypeManifolds()
  {
    return Collections.emptySet();
  }
}
