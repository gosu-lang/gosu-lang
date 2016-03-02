/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.dynamic;

import gw.lang.reflect.BaseTypeInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IExpando;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class DynamicTypeInfo extends BaseTypeInfo
{
  private Map<CharSequence, IPropertyInfo> _piCache;
  private List<DynamicConstructorInfo> _dci;

  public DynamicTypeInfo( IType type )
  {
    super( type );
    _piCache = new HashMap<>();
    _dci = Arrays.asList(
      new DynamicConstructorInfo( this ),
      new DynamicConstructorInfo( this, TypeSystem.get( IExpando.class ) ) );
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors() {
    return _dci;
  }

  @Override
  public IConstructorInfo getConstructor( IType... params ) {
    if( params == null || params.length == 0 ) {
      return _dci.get( 0 );
    }
    else if( params.length == 1 && TypeSystem.get( IExpando.class ) == params[0] ) {
      return _dci.get( 1 );
    }
    return null;
  }

  @Override
  public IConstructorInfo getCallableConstructor( IType... params ) {
    if( params == null || params.length == 0 ) {
      return _dci.get( 0 );
    }
    else if( params.length == 1 && TypeSystem.get( IExpando.class ).isAssignableFrom( params[0] ) ) {
      return _dci.get( 1 );
    }
    return null;
  }

  @Override
  public IPropertyInfo getProperty( CharSequence propName )
  {
    IPropertyInfo pi = _piCache.get( propName );
    if( pi == null )
    {
      TypeSystem.lock();
      try
      {
        pi = _piCache.get( propName );
        if( pi == null )
        {
          pi = new DynamicPropertyInfo( this, propName.toString() );
          _piCache.put( propName, pi );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return pi;
  }

  @Override
  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    // Probably should cache these, but doing so could be more expensive e.g., the lookup involves param types
    return new DynamicMethodInfo( this, methodName.toString(), params );
  }

  @Override
  public IMethodInfo getCallableMethod( CharSequence method, IType... params )
  {
    return new DynamicMethodInfo( this, method.toString(), params );
  }
}
