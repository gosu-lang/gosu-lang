/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.ISource;
import manifold.api.fs.IFile;
import manifold.api.sourceprod.ClassType;
import manifold.api.sourceprod.ISourceProducer;

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

  default ISourceProducer getSourceProducer()
  {
    return null;
  }
}
