/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.IStatement;

public interface IClassFileStatement extends IStatement
{
  IClassStatement getClassStatement();

  IGosuClass getGosuClass();
    
}
