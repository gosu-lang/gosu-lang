/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.annotation.Annotations;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.gs.IGosuClass;

import java.util.Collection;
import java.util.Iterator;

class StandardGosuAnnotationConstructor implements IConstructorHandler {
  private IGosuClass _gsClass;
  private Collection<DynamicFunctionSymbol> _methods;

  public StandardGosuAnnotationConstructor( IGosuClass gsClass, Collection<DynamicFunctionSymbol> methods ) {
    _gsClass = gsClass;
    _methods = methods;
  }

  @Override
  public Object newInstance( Object... args ) {
    Class<?> javaClass = _gsClass.getBackingClass();
    Annotations.Builder builder = Annotations.builder( (Class)javaClass );
    Iterator<DynamicFunctionSymbol> methods = _methods.iterator();
    for( int i = 0; i < args.length; i++ ) {
      builder.withElement( methods.next().getDisplayName(), args[i] );
    }
    return builder.create();
  }
}
