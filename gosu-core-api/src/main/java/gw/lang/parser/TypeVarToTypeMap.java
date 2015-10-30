/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class TypeVarToTypeMap
{
  public static final TypeVarToTypeMap EMPTY_MAP = new TypeVarToTypeMap( Collections.<ITypeVariableType, IType>emptyMap() );

  private Map<ITypeVariableType, IType> _map;
  private Set<ITypeVariableType> _typesInferredFromCovariance;
  private boolean _bStructural;

  public TypeVarToTypeMap()
  {
    _map = new LinkedHashMap<>( 2 );
    _typesInferredFromCovariance = new HashSet<>( 2 );
  }

  private TypeVarToTypeMap( Map<ITypeVariableType, IType> emptyMap )
  {
    _map = emptyMap;
    _typesInferredFromCovariance = new HashSet<>( 2 );
  }

  public TypeVarToTypeMap( TypeVarToTypeMap from )
  {
    this();
    _map.putAll( from._map );
    _typesInferredFromCovariance = new HashSet<>( 2 );
    _typesInferredFromCovariance.addAll( from._typesInferredFromCovariance );
    _bStructural = from._bStructural;
  }

  public IType get( ITypeVariableType tvType )
  {
    return _map.get( tvType );
  }

  public <E> IType getByMatcher( E tv, ITypeVarMatcher<E> matcher )
  {
    for( ITypeVariableType key : _map.keySet() )
    {
      if( matcher.matches( tv, key ) )
      {
        return _map.get( key );
      }
    }
    return null;
  }

  public IType getByString( String tv )
  {
    for( ITypeVariableType key: _map.keySet() )
    {
      if( tv.equals( key.getRelativeName() ) || tv.equals( key.getName() ) )
      {
        return key;
      }
    }
    return null;
  }

  public boolean containsKey( ITypeVariableType tvType )
  {
    return _map.containsKey( tvType );
  }

  public IType put( ITypeVariableType tvType, IType type )
  {
    IType existing = remove( tvType );
    _map.put( tvType, type );
    return existing;
  }

  public void putAll( TypeVarToTypeMap from )
  {
    for( ITypeVariableType x : from._map.keySet() )
    {
      put( x, from.get( x ) );
    }
  }

  public void putAllAndInferred( TypeVarToTypeMap from )
  {
    for( ITypeVariableType x : from._map.keySet() )
    {
      put( x, from.get( x ) );
    }
    _typesInferredFromCovariance.addAll( from._typesInferredFromCovariance );
  }

  public boolean isEmpty()
  {
    return _map.isEmpty();
  }

  public int size()
  {
    return _map.size();
  }

  public Set<ITypeVariableType> keySet()
  {
    return _map.keySet();
  }

  public Set<Map.Entry<ITypeVariableType,IType>> entrySet()
  {
    return _map.entrySet();
  }

  public IType remove( ITypeVariableType tvType )
  {
    return _map.remove( tvType );
  }

  public Collection<IType> values()
  {
    return _map.values();
  }

  public boolean isStructural() {
    return _bStructural;
  }
  public void setStructural( boolean bStructural ) {
    _bStructural = bStructural;
  }

  public boolean isInferredForCovariance( ITypeVariableType tv ) {
    return !isStructural() || _typesInferredFromCovariance.contains( tv );
  }
  public void setInferredForCovariance( ITypeVariableType tv ) {
    _typesInferredFromCovariance.add( tv );
  }

  public interface ITypeVarMatcher<E> {
    boolean matches( E thisOne, ITypeVariableType thatOne );
  }
}
