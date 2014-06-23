/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IStatement;

import java.util.List;

public interface IClasspathStatement extends IStatement
{
  String getClasspath();

  void setClasspath( String classpath );

  List<String> getPaths();
}