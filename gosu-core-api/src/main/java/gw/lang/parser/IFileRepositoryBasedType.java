/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.ISourceFileHandle;

public interface IFileRepositoryBasedType extends IType
{
  ISourceFileHandle getSourceFileHandle();
  ClassType getClassType();
}
