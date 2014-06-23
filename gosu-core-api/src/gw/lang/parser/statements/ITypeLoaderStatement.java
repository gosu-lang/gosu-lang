/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IStatement;
import gw.lang.reflect.IType;

public interface ITypeLoaderStatement extends IStatement
{
  IType getTypeLoader();
}