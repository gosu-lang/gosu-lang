/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract public class ByteCodeTestBase extends TestClass
{

  /**
   * Invokes the method named 'methodName' on the 'ctx' object, with the args 'args'
   * Note that the types of the arguments in 'args' must line up exactly with the types
   * of the method.  METHOD OVERLOADING!
   */
  public final Object invokeMethod( Object ctx, String methodName, Object... args )
  {
    Class<? extends Object> aClass = ctx.getClass();
    return invokeMethodImpl( aClass, ctx, methodName, args );
  }

  public Object invokeStaticMethod( String className, String methodName, Object... args )
  {
    try
    {
      return invokeMethodImpl( GosuClassLoader.instance().findClass( className ), null, methodName, args );
    }
    catch( ClassNotFoundException e )
    {
      throw new RuntimeException( e );
    }
  }

  private Object invokeMethodImpl( Class<? extends Object> aClass, Object ctx, String methodName, Object... args )
  {
    Object val;
    try
    {
      Method m = aClass.getMethod( methodName, getClasses(args));
      val = m.invoke( ctx, args );
    }
    catch( NoSuchMethodException e )
    {
      throw new RuntimeException( e );
    }
    catch( IllegalAccessException e )
    {
      throw new RuntimeException( e );
    }
    catch( InvocationTargetException e )
    {
      throw new RuntimeException( e );
    }
    return val;
  }

  private Class<?>[] getClasses( Object[] args )
  {
    Class<?>[] classes = new Class<?>[args.length];
    for( int i = 0; i < args.length; i++ )
    {
      classes[i] = args[i].getClass();
    }
    return classes;
  }

  public static Object constructFromGosuClassloader( String name )
  {
    try
    {
      Class<?> javaClass = GosuClassLoader.instance().findClass( name );
      assertNotNull( javaClass );
//      assertEquals( name, javaClass.getName() ); not true for inner classes
      Object o = javaClass.newInstance();
      assertNotNull( o );
      return o;
    }
    catch( ClassNotFoundException e )
    {
      throw new RuntimeException( e );
    }
    catch( InstantiationException e )
    {
      throw new RuntimeException( e );
    }
    catch( IllegalAccessException e )
    {
      throw new RuntimeException( e );
    }
  }

  protected Class<?> getClassFromGosuClassLoader( String cls )
    throws ClassNotFoundException
  {
    return GosuClassLoader.instance().findClass( cls );
  }

}
