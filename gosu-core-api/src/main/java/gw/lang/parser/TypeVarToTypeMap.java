/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class TypeVarToTypeMap
{
  public static final TypeVarToTypeMap EMPTY_MAP = new TypeVarToTypeMap( true );

  private Map<Object, IType> _map;


  public TypeVarToTypeMap()
  {
    this( false );
  }
  public TypeVarToTypeMap( boolean bEmpty )
  {
    if( bEmpty )
    {
      _map = Collections.emptyMap();
    }
    else
    {
      _map = new LinkedHashMap<Object, IType>( 2 );
    }
  }

  public TypeVarToTypeMap( TypeVarToTypeMap actualParamByVarName )
  {
    this();
    _map.putAll( actualParamByVarName._map );
  }

  public IType get( ITypeVariableType tvType )
  {
    IType type = _map.get( tvType );
    if( type == null )
    {
      for( Object key : _map.keySet() )
      {
        if( looseEquals( tvType, key ) )
        {
          type = _map.get( key );
          break;
        }
      }
    }
    return type;
  }

  public IType getByString( String strName )
  {
    IType type = _map.get( strName );
    if( type == null )
    {
      for( Object key : _map.keySet() )
      {
        if( looseEquals( key, strName ) )
        {
          type = _map.get( key );
          break;
        }
      }
    }
    return type;
  }

  public IType getRaw( Object o )
  {
    IType type = _map.get( o );
    if( type == null )
    {
      for( Object key : _map.keySet() )
      {
        if( looseEquals( key, o ) )
        {
          type = _map.get( key );
          //break; don't break here as we want to use the last match
        }
      }
    }
    return type;
  }


  public boolean containsKey( ITypeVariableType tvType )
  {
    if( _map.containsKey( tvType ) )
    {
      return true;
    }
    for( Object key : _map.keySet() )
    {
      if( looseEquals( key, tvType ) )
      {
        return true;
      }
    }
    return false;
  }

  public boolean containsKeyRaw( Object o )
  {
    if( _map.containsKey( o ) )
    {
      return true;
    }
    for( Object key : _map.keySet() )
    {
      if( looseEquals( key, o ) )
      {
        return true;
      }
    }
    return false;
  }

  public static boolean looseEquals( Object p1, Object p2 )
  {
    if( p1 instanceof ITypeVariableType )
    {
      if( p2 instanceof ITypeVariableType )
      {
        return p1.equals( p2 );
      }
      if( p2 instanceof String )
      {
        ITypeVariableType tvType = (ITypeVariableType)p1;
        return p2.equals( tvType.getRelativeName() ) ||
               p2.equals( tvType.getName() );
      }
    }
    else if( p1 instanceof String )
    {
      if( p2 instanceof String )
      {
        return p1.equals( p2 );
      }
      if( p2 instanceof ITypeVariableType )
      {
        ITypeVariableType tvType = (ITypeVariableType)p2;
        return p1.equals( tvType.getRelativeName() ) ||
               p1.equals( tvType.getName() );
      }
    }
    throw new IllegalStateException( "Unexpected types: " + p1.getClass() + ", " + p2.getClass() );
  }

  public IType put( ITypeVariableType tvType, IType type )
  {
    IType existing = remove( tvType );
    _map.put( tvType, type );
    return existing;
  }

  public void putAll( TypeVarToTypeMap from )
  {
    for( Object x : from._map.keySet() )
    {
      putRaw( x, from.getRaw( x ) );
    }
  }

  public IType putByString( String strName, IType object )
  {
    return _map.put( strName, object );
  }

  public IType putRaw( Object o, IType type )
  {
    return _map.put( o, type );
  }

  public boolean isEmpty()
  {
    return _map.isEmpty();
  }

  public int size()
  {
    return _map.size();
  }

  public Set<Object> keySet()
  {
    return _map.keySet();
  }

  public Set<Map.Entry<Object,IType>> entrySet()
  {
    return _map.entrySet();
  }

  public IType remove( ITypeVariableType tvType )
  {
    IType type = _map.remove( tvType );
    if( type == null )
    {
      for( Iterator iter = _map.keySet().iterator(); iter.hasNext(); )
      {
        Object key = iter.next();
        if( looseEquals( tvType, key ) )
        {
          type = _map.get( key );
          iter.remove();
          break;
        }
      }
    }
    return type;
  }

  public IType removeByString( String strName )
  {
    return _map.remove( strName );
  }

  public Collection<IType> values()
  {
    return _map.values();
  }
}
