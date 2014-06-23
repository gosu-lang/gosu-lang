/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import java.util.LinkedList;
import java.util.List;

/**
 */
public class GosuClassCompilingStack
{
  private static final ThreadLocal<LinkedList<IType>> g_compilingClassStack = new ThreadLocal<LinkedList<IType>>();

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
      list = new LinkedList<IType>();
      g_compilingClassStack.set(list);
    }
    TypeSystem.pushModule(gsClass.getTypeLoader().getModule());
    list.add( 0, gsClass );
  }

  public static void popCompilingType()
  {
    List<IType> list = g_compilingClassStack.get();
    if( list != null )
    {
      IType type = list.remove( 0 );
      TypeSystem.popModule( type.getTypeLoader().getModule() );
    }
  }
}
