/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IStatement;
import gw.lang.reflect.gs.IGosuClass;

public interface IClassStatement extends IStatement
{
  IClassFileStatement getClassFileStatement();

  IGosuClass getGosuClass();

  IClassDeclaration getClassDeclaration();
}
