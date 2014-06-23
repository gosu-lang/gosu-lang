/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.javadoc.IDocRef;
import gw.lang.javadoc.IMethodNode;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IMethodBackedPropertyInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

public interface IJavaPropertyInfo extends IAttributedFeatureInfo, IPropertyInfo, IJavaBasePropertyInfo, IMethodBackedPropertyInfo
{
  IType getGenericIntrinsicType();

  IDocRef<IMethodNode> getMethodDocs();

  String getShortDescription();

  IMethodInfo getReadMethodInfo();

  IMethodInfo getWriteMethodInfo();

  IJavaPropertyDescriptor getPropertyDescriptor();

}
