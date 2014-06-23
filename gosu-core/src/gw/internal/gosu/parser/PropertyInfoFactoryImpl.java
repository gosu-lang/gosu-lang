/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IPropertyInfoFactory;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.IPresentationInfo;

import java.beans.IntrospectionException;

/**
 */
public class PropertyInfoFactoryImpl implements IPropertyInfoFactory
{

  public IPropertyInfo make( IFeatureInfo container, String strName, Class javaClass, String strGetter, String strSetter, IType propertyType ) throws IntrospectionException
  {
    return PropertyInfoFactory.make( container, strName, javaClass, strGetter, strSetter, propertyType );
  }

  public IPropertyInfo make( IFeatureInfo container, String strName, Class javaClass, String strGetter, String strSetter, IType propertyType, IPresentationInfo presInfo ) throws IntrospectionException
  {
    return PropertyInfoFactory.make( container, strName, javaClass, strGetter, strSetter, propertyType, presInfo );
  }

}
