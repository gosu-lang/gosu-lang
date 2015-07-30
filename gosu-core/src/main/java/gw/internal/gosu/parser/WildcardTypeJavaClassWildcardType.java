/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.coercers.FunctionToInterfaceCoercer;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassWildcardType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.WildcardType;
import java.lang.reflect.Type;

public class WildcardTypeJavaClassWildcardType extends TypeJavaClassType implements IJavaClassWildcardType {
  private Type _genType;
  private WildcardType _wildcardType;

  public WildcardTypeJavaClassWildcardType( Type genType, WildcardType wildcardType, IModule module ) {
    super(wildcardType, module);
    _genType = genType;
    _wildcardType = wildcardType;
  }

  @Override
  public IJavaClassType getUpperBound() {
    // we only support one bound in Gosu

    if( maybeUseLowerBoundForFunctionalInterface() )
    {
      return TypeJavaClassType.createType( _wildcardType.getLowerBounds()[0], _module );
    }

    Type rawType = _wildcardType.getUpperBounds()[0];
    return TypeJavaClassType.createType(rawType, _module);
  }

  private boolean maybeUseLowerBoundForFunctionalInterface()
  {
    if( _genType instanceof Class && ((Class)_genType).isInterface() && _wildcardType.getLowerBounds().length > 0 ) {
      if( FunctionToInterfaceCoercer.getSingleMethodFromJavaInterface( (IJavaType)TypeSystem.get( (Class)_genType, getModule() ) ) != null ) {
        // Functional interfaces parameterized with ? super T wildcard type keep T so contravariance works with blocks
        return true;
      }
    }
    return false;
  }

  @Override
  public IJavaClassType getConcreteType() {
    return getUpperBound();
  }

  @Override
  public String getSimpleName() {
    return getName();
  }

  @Override
  public IModule getModule() {
    return _module;
  }
}