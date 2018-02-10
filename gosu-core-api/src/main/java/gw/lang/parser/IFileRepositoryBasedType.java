/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import manifold.api.type.ClassType;
import gw.lang.reflect.gs.ISourceFileHandle;

public interface IFileRepositoryBasedType extends IType
{
  ISourceFileHandle getSourceFileHandle();
  ClassType getClassType();

  default String getJavaName()
  {
    IType type = TypeSystem.getPureGenericType( this );
    IType outerType = type.getEnclosingType();
    if( outerType instanceof IFileRepositoryBasedType )
    {
      return ((IFileRepositoryBasedType)outerType).getJavaName() + "$" + type.getRelativeName();
    }
    return type.getName();
  }
}
