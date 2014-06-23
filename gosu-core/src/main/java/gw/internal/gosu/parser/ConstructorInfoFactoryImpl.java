/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IConstructorInfoFactory;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IConstructorType;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.javadoc.IConstructorNode;
import gw.internal.gosu.parser.types.ConstructorType;

import java.lang.reflect.Constructor;

/**
 */
public class ConstructorInfoFactoryImpl implements IConstructorInfoFactory
{
  @Override
  public IConstructorInfo create( IFeatureInfo container, IJavaClassConstructor ctor, IConstructorNode docs )
  {
    return new JavaConstructorInfo( container, ctor);
  }

  @Override
  public IConstructorType makeConstructorType( IConstructorInfo ctor )
  {
    return new ConstructorType( ctor );
  }
}