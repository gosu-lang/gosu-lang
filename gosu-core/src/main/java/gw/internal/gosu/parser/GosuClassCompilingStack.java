/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IType;

import java.util.LinkedList;
import java.util.List;

/**
 */
public class GosuClassCompilingStack
{
  private static final ThreadLocal<LinkedList<IType>> g_compilingClassStack = new ThreadLocal<>();

  public static IType getCurrentCompilingType()
  {
    LinkedList<IType> list = g_compilingClassStack.get();
    return list != null && !list.isEmpty() ? list.getFirst() : null;
  }

  /**
   */
  public static IType getCompilingType( String typeName )
  {
    List<IType> list = g_compilingClassStack.get();
    if( list != null )
    {
      for( int i = 0; i < list.size(); i++ )
      {
        IType type = list.get( i );
        if( type.getName().equals( typeName ) )
        {
          return type;
        }
      }
    }
    return null;
  }

  public static void pushCompilingType( IType gsClass )
  {
    LinkedList<IType> list = g_compilingClassStack.get();
    if( list == null )
    {
      list = new LinkedList<>();
      g_compilingClassStack.set(list);
    }
    list.add( 0, gsClass );
  }

  public static void popCompilingType()
  {
    List<IType> list = g_compilingClassStack.get();
    if( list != null )
    {
      list.remove( 0 );
    }
  }
}
