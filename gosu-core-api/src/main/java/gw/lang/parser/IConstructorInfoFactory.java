/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.javadoc.IConstructorNode;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IConstructorType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.java.IJavaClassConstructor;

public interface IConstructorInfoFactory
{
  IConstructorInfo create( IFeatureInfo container, IJavaClassConstructor ctor, IConstructorNode docs );

  IConstructorType makeConstructorType( IConstructorInfo ctor );
}
