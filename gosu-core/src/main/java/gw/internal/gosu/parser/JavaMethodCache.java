/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.util.concurrent.ConcurrentWeakHashMap;
import gw.util.concurrent.LockingLazyVar;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class JavaMethodCache
{
  private static final Map<Class, Method[]> DECLARED_METHOD_CACHE = new ConcurrentWeakHashMap<>( 100 );

  private static LockingLazyVar<DeclaredMethodsAccessor> _declaredMethodsAccessor =
    new LockingLazyVar<DeclaredMethodsAccessor>()
    {
      protected DeclaredMethodsAccessor init()
      {
        try
        {
          Method method = Class.class.getDeclaredMethod( "privateGetDeclaredMethods", boolean.class );
          return new PrivateGetDeclaredMethodsAccessor( method );
        }
        catch( Exception e )
        {
          return new PublicGetDeclaredMethodsAccessor();
        }
      }
    };


  /**
   * This method is not normally required.  It is normally only needed by advanced
   * tools that update existing "Class" objects in-place and need to
   * re-analyze existing Class objects.
   */
  public static void flushCaches()
  {
    DECLARED_METHOD_CACHE.clear();
  }

  public static Method[] getDeclaredMethods( Class clz )
  {
    // Looking up Class.getDeclaredMethods is relatively expensive, so we cache the results.
    Method[] result = DECLARED_METHOD_CACHE.get( clz );
    if( result != null )
    {
      return result;
    }

    result = _declaredMethodsAccessor.get().getDeclaredMethods( clz );
    Arrays.sort( result, ( o1, o2 ) -> {
      int res = o1.getName().compareTo( o2.getName() );
      if( res == 0 )
      {
        // We want bridge methods to be the last ones. They have less concrete return types.
        boolean b1 = o1.isBridge();
        boolean b2 = o2.isBridge();
        if( b1 != b2 )
        {
          res = b1 ? 1 : -1;
        }
      }
      return res;
    } );

    // Add it to the cache.
    DECLARED_METHOD_CACHE.put( clz, result );
    return result;
  }

  private static interface DeclaredMethodsAccessor
  {
    Method[] getDeclaredMethods( Class clz );
  }

  private static class PrivateGetDeclaredMethodsAccessor implements DeclaredMethodsAccessor
  {
    private final Method _method;

    public PrivateGetDeclaredMethodsAccessor( Method method )
    {
      _method = method;
      _method.setAccessible( true );
    }

    @Override
    public Method[] getDeclaredMethods( final Class clz )
    {
      try
      {
        Method[] result = (Method[])_method.invoke( clz, false );
        Method[] copy = new Method[result.length]; // copy so as not to mess up the Class' method offsets
        System.arraycopy( result, 0, copy, 0, copy.length );
        return copy;
      }
      catch( Exception e )
      {
        System.err.println( "WARNING Cannot load methods of " + clz.getName() + ": " + getRootCause( e ).toString() );
        return new Method[0];
      }
    }
  }

  /**
   * Traverse exception chain to return the root cause.
   *
   * @param throwable The top-level exception in the chain
   *
   * @return The root (or top-level if none chained)
   */
  public static Throwable getRootCause( Throwable throwable )
  {
    Throwable cause = throwable;
    while( cause.getCause() != null )
    {
      cause = cause.getCause();
    }
    return cause;
  }

  public static class PublicGetDeclaredMethodsAccessor implements DeclaredMethodsAccessor
  {
    @Override
    public Method[] getDeclaredMethods( Class clz )
    {
      return clz.getDeclaredMethods();
    }
  }
}
