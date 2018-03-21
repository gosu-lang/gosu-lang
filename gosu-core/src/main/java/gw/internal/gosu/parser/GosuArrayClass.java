/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.DefaultArrayType;
import gw.lang.reflect.gs.IGosuArrayClass;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.util.Pair;
import gw.util.concurrent.LockingLazyVar;

import gw.util.Array;

/**
 */
public class GosuArrayClass extends DefaultArrayType implements IGosuArrayClass
{
  // lazy to break circular reference issues
  private LockingLazyVar<IJavaClassInfo> _lazyConcreteClass;

  public GosuArrayClass( final IType componentType, ITypeLoader typeLoader )
  {
    super( componentType, null, typeLoader );
    _lazyConcreteClass =
      new LockingLazyVar<IJavaClassInfo>()
      {
        @Override
        protected IJavaClassInfo init()
        {
          Class componentClass;
          if( componentType instanceof IGosuClass )
          {
            componentClass = ((IGosuClass)componentType).getBackingClass();
          }
          else
          {
            componentClass = ((GosuArrayClass) componentType).getConcreteClass().getBackingClass();
          }
          componentClass = Array.newInstance( componentClass, 0 ).getClass();
          return TypeSystem.getJavaClassInfo( componentClass );
        }
      };
  }

  @Override
  protected IGosuArrayClass makeArrayType()
  {
    return (IGosuArrayClass)TypeSystem.getOrCreateTypeReference(
      new GosuArrayClass( TypeSystem.getOrCreateTypeReference( this ), getTypeLoader() ) );
  }

  @Override
  public Object makeArrayInstance( int iLength )
  {
    Pair<Integer, Class> gosuClassDepthPair = getGosuClassDepthPair( getComponentType(), 2 );
    Integer first = gosuClassDepthPair.getFirst();
    return Array.newInstance( gosuClassDepthPair.getSecond(), getDims( first, iLength ) );
  }

  private int[] getDims( Integer depth, int iLength )
  {
    int[] ints = new int[depth];
    for( int i = 0; i < depth; i++ )
    {
      if( i == 0 )
      {
        ints[i] = iLength;
      }
      else
      {
        ints[i] = 0;        
      }
    }
    return ints;
  }

  @Override
  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return Array.get( array, iIndex );
  }

  @Override
  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    Array.set( array, iIndex, value );
  }

  @Override
  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return Array.getLength( array );
  }

  public boolean hasGosuClassAtRoot( IType component )
  {
    return getGosuClassDepthPair( component, 2 ) != null;
  }

  @Override
  public IJavaClassInfo getConcreteClass()
  {
    return _lazyConcreteClass.get();
  }

  private Pair<Integer, Class> getGosuClassDepthPair( IType component, int depth )
  {
    if( component == null )
    {
      return null;
    }
    else if( component instanceof IGosuClass )
    {
      return new Pair<Integer, Class>( depth, ((IGosuClassInternal) component).getBackingClass() );
    }
    else
    {
      return getGosuClassDepthPair( component.getComponentType(), depth + 1 );
    }
  }
}
