/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.test.TestClass;
import gw.lang.parser.EvaluationException;

import java.lang.reflect.Method;

/**
 */
public class MethodCallAdapterTest extends TestClass
{
  public void testCallStaticMethod() throws Exception
  {
    Method m = MethodCallAdapterTest.class.getMethod( "staticMethod", String.class );

    MethodCallAdapter mc = new MethodCallAdapter( m );
    String strResult = (String)mc.handleCall( null, "hello" );
    assertEquals( "static hello", strResult );
  }

  public void testCallNonStaticMethod() throws Exception
  {
    Method m = MethodCallAdapterTest.class.getMethod( "nonstaticMethod", String.class );

    MethodCallAdapter mc = new MethodCallAdapter( m );
    String strResult = (String)mc.handleCall( this, "hello" );
    assertEquals( "nonstatic hello", strResult );
  }

  public void testThrowsExceptionForNullRef() throws Exception
  {
    Method m = MethodCallAdapterTest.class.getMethod( "nonstaticMethod", String.class );

    MethodCallAdapter mc = new MethodCallAdapter( m );
    try
    {
      mc.handleCall( null, "hello" );
      fail();
    }
    catch( EvaluationException e )
    {
      assertTrue( e.getMessage().startsWith( "Tried to invoke method from null reference" ) );
      return;
    }
    fail();
  }

  public void testThrowsExceptionForIncompatibleRef() throws Exception
  {
    Method m = MethodCallAdapterTest.class.getMethod( "nonstaticMethod", String.class );

    MethodCallAdapter mc = new MethodCallAdapter( m );
    try
    {
      mc.handleCall( new Object(), "hello" );
      fail();
    }
    catch( EvaluationException e )
    {
      assertTrue( e.getMessage().startsWith( "Tried to invoke method from a context not compatible with method's declaring class." ) );
      return;
    }
    fail();
  }

  public static String staticMethod( String str )
  {
    return "static " + str;
  }

  public String nonstaticMethod( String str )
  {
    return "nonstatic " + str;
  }
}
