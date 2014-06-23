/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.IJavaClassInfo;

public class FunctionArrayType extends DefaultNonLoadableArrayType
{
  public FunctionArrayType( IType componentType, IJavaClassInfo componentConcreteClass, ITypeLoader typeLoader )
  {
    super( componentType, componentConcreteClass, typeLoader );
  }
}
