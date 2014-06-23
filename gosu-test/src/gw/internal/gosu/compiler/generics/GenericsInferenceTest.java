/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.generics;

import gw.lang.reflect.gs.IGosuObject;
import gw.internal.gosu.compiler.ByteCodeTestBase;
import gw.internal.gosu.compiler.GosuClassLoader;

import java.lang.reflect.Method;


public class GenericsInferenceTest extends ByteCodeTestBase
{
  public void testInferFromMethodCall() throws Exception
  {
    IGosuObject obj = getInstance( "gw.internal.gosu.compiler.sample.statement.classes.GenericInferenceTestClass" );
    Method method = obj.getClass().getMethod( "getElemFromListOfStrings" );
    String elem = (String)method.invoke( obj );
    assertEquals( "hello", elem );
  }

  public void testInferFromBeanMethodCall() throws Exception
  {
    IGosuObject obj = getInstance( "gw.internal.gosu.compiler.sample.statement.classes.GenericInferenceTestClass" );
    Method method = obj.getClass().getMethod( "getCharFromElemFromListOfStrings" );
    Character elem = (Character)method.invoke( obj );
    assertEquals( Character.valueOf( 'h' ), elem );
  }

  public void testInferFromPropertyIdentifier() throws Exception
  {
    IGosuObject obj = getInstance( "gw.internal.gosu.compiler.sample.statement.classes.SubclassOfGenericBase" );
    Method method = obj.getClass().getMethod( "accessPropertyIdentifier" );
    String ret = (String)method.invoke( obj );
    assertEquals( "hello", ret );
  }

  private IGosuObject getInstance( String strClassName ) throws Exception
  {
    Class javaClass = GosuClassLoader.instance().findClass( strClassName );
    return (IGosuObject)javaClass.newInstance();
  }
}