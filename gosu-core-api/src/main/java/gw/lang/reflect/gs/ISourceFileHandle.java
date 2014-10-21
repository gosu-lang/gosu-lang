/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.fs.IFile;
import gw.lang.parser.ISource;

public interface ISourceFileHandle
{
  public ISource getSource();

  public String getParentType();

  String getNamespace();

  String getFilePath();

  boolean isTestClass();

  boolean isValid();

  boolean isStandardPath();

  public void cleanAfterCompile();

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
}
