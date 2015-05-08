/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class TypeVarToTypeMap
{
  public static final TypeVarToTypeMap EMPTY_MAP = new TypeVarToTypeMap( Collections.<ITypeVariableType, IType>emptyMap() );

  private Map<ITypeVariableType, IType> _map;


  public TypeVarToTypeMap()
  {
    _map = new LinkedHashMap<ITypeVariableType, IType>( 2 );
  }

  private TypeVarToTypeMap( Map<ITypeVariableType, IType> emptyMap )
  {
    _map = emptyMap;
  }

  public TypeVarToTypeMap( TypeVarToTypeMap actualParamByVarName )
  {
    this();
    _map.putAll( actualParamByVarName._map );
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

  public interface ITypeVarMatcher<E> {
    boolean matches( E thisOne, ITypeVariableType thatOne );
  }
}
