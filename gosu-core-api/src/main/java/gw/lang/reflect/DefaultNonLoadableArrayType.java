/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.IJavaClassInfo;

public class DefaultNonLoadableArrayType extends DefaultArrayType implements INonLoadableType
{
  public DefaultNonLoadableArrayType( IType componentType, IJavaClassInfo componentConcreteClass, ITypeLoader typeLoader )
  {
    super( componentType, componentConcreteClass, typeLoader );
  }

  @Override
  protected IDefaultArrayType makeArrayType()
  {
    return new DefaultNonLoadableArrayType( this, getConcreteClass(), getTypeLoader() );
  }
}
