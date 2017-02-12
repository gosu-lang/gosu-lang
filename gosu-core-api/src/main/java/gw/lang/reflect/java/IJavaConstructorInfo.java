/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.javadoc.JavaHasParams;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;

import gw.lang.reflect.IOptionalParamCapable;
import java.lang.reflect.Constructor;

public interface IJavaConstructorInfo extends IAttributedFeatureInfo, IConstructorInfo, IOptionalParamCapable, JavaHasParams
{
  IJavaClassConstructor getJavaConstructor();

  Constructor getRawConstructor();

  boolean isDefault();
}
