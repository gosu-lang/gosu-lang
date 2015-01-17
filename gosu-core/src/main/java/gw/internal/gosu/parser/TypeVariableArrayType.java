/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.DefaultArrayType;
import gw.lang.reflect.ITypeVariableArrayType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IDefaultArrayType;
import gw.lang.reflect.IEnhanceableType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.DefaultNonLoadableArrayType;
import gw.lang.reflect.java.IJavaClassInfo;

/**
 */
public class TypeVariableArrayType extends DefaultNonLoadableArrayType implements ITypeVariableArrayType, IEnhanceableType
{
  public TypeVariableArrayType( ITypeVariableType componentType, IJavaClassInfo componentConcreteClass, ITypeLoader typeLoader )
  {
    super( componentType, componentConcreteClass, typeLoader );
  }

  @Override
  public Object makeArrayInstance( int iLength )
  {
    throw new UnsupportedOperationException( "Unable to create new instances of type variable arrays due to the Gosu array/Java array distinction" );
  }

  @Override
  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    IType fromObject = TypeSystem.getFromObject( array );
    return fromObject.getArrayComponent( array, iIndex );
  }

  @Override
  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    IType fromObject = TypeSystem.getFromObject( array );
    fromObject.setArrayComponent( array, iIndex, value );
  }

  @Override
  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    IType fromObject = TypeSystem.getFromObject( array );
    return fromObject.getArrayLength( array );
  }

  @Override
  public boolean equals(Object obj) {
    if ( !(obj instanceof TypeVariableArrayType) ) {
      return false;
    }
    return getComponentType().equals( ((TypeVariableArrayType)obj).getComponentType() );
  }

  @Override
  public boolean isAssignableFrom( IType type )
  {
    if( type == this )
    {
      return true;
    }

    return type.isArray() && getComponentType().isAssignableFrom( type.getComponentType() );
  }
}
