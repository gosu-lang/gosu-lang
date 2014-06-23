/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.List;

public interface IXmlTypeData extends ITypeInfo {

  String getName();

  IType getType();

  List<? extends IPropertyInfo> getDeclaredProperties();

  List<? extends IMethodInfo> getDeclaredMethods();

  List<? extends IConstructorInfo> getDeclaredConstructors();

  boolean isFinal();

  boolean isEnum();

  IType getSuperType();

  List<Class<?>> getAdditionalInterfaces();

  boolean prefixSuperProperties();

  IType getSupertypeToCopyPropertiesFrom();

  List<? extends IType> getInterfaces();

  long getFingerprint();

  Class<?> getBackingClass();

  IJavaClassInfo getBackingClassInfo();

  XmlSchemaIndex<?> getSchemaIndex();
}
