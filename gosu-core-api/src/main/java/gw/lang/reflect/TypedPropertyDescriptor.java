/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class TypedPropertyDescriptor extends PropertyDescriptor implements IIntrinsicTypeReference
{
  private Class _propertyClass;

  /**
   * Used to construct a property descriptor for extension fields.
   */
  public TypedPropertyDescriptor( String propertyName,
                                      Class beanClass,
                                      String getterName,
                                      String setterName ) throws IntrospectionException
  {
    super( propertyName, beanClass, getterName, setterName );
    _propertyClass = super.getPropertyType();
  }

  public Class getPropertyType()
  {
    return _propertyClass;
  }

  // Overwrite setRead/WriteMethod() methods to make sure propertyType
  // is correctly set when these are called. This is necessary since
  // this class maintains its own copy of propertyType.
  public void setReadMethod( Method getter ) throws IntrospectionException
  {
    super.setReadMethod( getter );
    if( _propertyClass == null )
    {
      _propertyClass = super.getPropertyType();
    }
  }

  public void setWriteMethod( Method setter ) throws IntrospectionException
  {
    super.setWriteMethod( setter );
    if( _propertyClass == null )
    {
      _propertyClass = super.getPropertyType();
    }
  }

  //----------------------------------------------------------------------------
  // -- ITypedFeatureInfo implementation --

  public IType getFeatureType()
  {
    return TypeSystem.get( getPropertyType() );
  }

}
