/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.UnstableAPI;

import java.beans.IntrospectionException;

@UnstableAPI
public interface IPropertyInfoFactory
{

  public IPropertyInfo make( IFeatureInfo container, String strName, Class javaClass, String strGetter, String strSetter, IType propertyType ) throws IntrospectionException;

  public IPropertyInfo make( IFeatureInfo container, String strName, Class javaClass, String strGetter, String strSetter, IType propertyType, IPresentationInfo presInfo ) throws IntrospectionException;
}
