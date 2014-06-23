/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.BeanInfoUtil;

import gw.lang.reflect.IType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IPresentationInfo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 */
public class PropertyInfoFactory
{

  public static IPropertyInfo make( IFeatureInfo container, String strName, Class javaClass,
                                    String strGetter, String strSetter, IType propertyType )
    throws IntrospectionException
  {
    PropertyDescriptor property = new PropertyDescriptor( strName, javaClass, strGetter, strSetter );
    BeanInfoUtil.makeScriptable( property );
    return new JavaPropertyInfo( container, new PropertyDescriptorJavaPropertyDescriptor(property, propertyType.getTypeLoader().getModule()), propertyType );
  }

  public static IPropertyInfo make( IFeatureInfo container, String strName, Class javaClass,
                                    String strGetter, String strSetter, IType propertyType,
                                    IPresentationInfo presInfo )
    throws IntrospectionException
  {
    PropertyDescriptor property = new PropertyDescriptor( strName, javaClass, strGetter, strSetter );
    BeanInfoUtil.makeScriptable( property );
    return new JavaPropertyInfo( container, new PropertyDescriptorJavaPropertyDescriptor(property, propertyType.getTypeLoader().getModule()), propertyType, presInfo );
  }

}
