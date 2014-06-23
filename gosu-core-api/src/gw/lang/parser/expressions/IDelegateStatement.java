/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.reflect.IType;

import java.util.List;

public interface IDelegateStatement extends IVarStatement
{
  
  List<IType> getConstituents();

}
