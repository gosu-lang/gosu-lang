/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.function.IBlock;

import javax.script.SimpleBindings;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 */
public class Expando extends SimpleBindings implements IExpando
{
  public Expando()
  {
    super( new LinkedHashMap<>() );
  }

  public Expando( Map<String, Object> map )
  {
    super( map );
  }

  @Override
  public Object getFieldValue( String field )
  {
    return get( field );
  }

  @Override
  public void setFieldValue( String field, Object value )
  {
    put( field, value );
  }

  @Override
  public Object invoke( String methodName, Object... args )
  {
    Object f = get( methodName );
    if( f instanceof IBlock )
    {
      return ((IBlock)f).invokeWithArgs( args );
    }
    return IPlaceholder.UNHANDLED;
  }

  @Override
  public void setDefaultFieldValue( String name )
  {
    setFieldValue( name, new Expando() );
  }

  @Override
  public boolean equals( Object o )
  {
    if( o == this )
    {
      return true;
    }

    if( !(o instanceof Map) )
    {
      return false;
    }
    Map<?, ?> m = (Map<?, ?>)o;
    if( m.size() != size() )
    {
      return false;
    }

    try
    {
      Iterator<Entry<String, Object>> i = entrySet().iterator();
      while( i.hasNext() )
      {
        Entry<String, Object> e = i.next();
        String key = e.getKey();
        Object value = e.getValue();
        if( value == null )
        {
          if( !(m.get( key ) == null && m.containsKey( key )) )
          {
            return false;
          }
        }
        else
        {
          if( !value.equals( m.get( key ) ) )
          {
            return false;
          }
        }
      }
    }
    catch( ClassCastException unused )
    {
      return false;
    }
    catch( NullPointerException unused )
    {
      return false;
    }

    return true;
  }


  @Override
  public String toString()
  {
    Iterator<Entry<String, Object>> i = entrySet().iterator();
    if( !i.hasNext() )
    {
      return "{}";
    }

    StringBuilder sb = new StringBuilder();
    sb.append( '{' );
    while( true )
    {
      Entry<String, Object> e = i.next();
      String key = e.getKey();
      Object value = e.getValue();
      sb.append( key );
      sb.append( " -> " );
      sb.append( value == this ? "(this Expando)" : value ).append( "\n" );
      if( !i.hasNext() )
      {
        return sb.append( '}' ).toString();
      }
    }
  }
}
