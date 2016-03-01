/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.generics;

import gw.lang.reflect.IType;
import gw.lang.reflect.ReflectUtil;
import gw.test.TestClass;

import java.util.Arrays;
import java.util.List;

/**
 */
public class GosuGenericMethodTest extends TestClass
{
  public void testInternalGenericMethodCall()
  {
    try
    {
      Object instance = ReflectUtil.construct(  "gw.internal.gosu.parser.generics.gwtest.InternalGenericMethodCall" );
      Object o = ReflectUtil.invokeMethod(instance, "getCount");
      assertEquals( 8, o );
    }
    catch( Exception e )
    {
      fail( e.getMessage() );
    }
  }

  public void testGenMethodWithBockArgUsingTypeVar()
  {
    try
    {
      Object instance = ReflectUtil.construct(  "gw.internal.gosu.parser.generics.gwtest.InternalGenericMethodCall" );
      Object o = ReflectUtil.invokeMethod(instance, "callGenMethodWithBockArgUsingTypeVar");
      assertListEquals( Arrays.asList( "hello", "hello" ), (List)o );
    }
    catch( Exception e )
    {
      fail( e.getMessage() );
    }
  }
  public void testLubInferenceAcrossArgs()
  {
    try
    {
      Object instance = ReflectUtil.construct(  "gw.internal.gosu.parser.generics.gwtest.InternalGenericMethodCall" );
      IType type = (IType)ReflectUtil.invokeMethod( instance, "lubInferenceAcrossArgsTest" );
      assertEquals( "java.io.Serializable & java.lang.CharSequence", type.getName() );
    }
    catch( Exception e )
    {
      fail( e.getMessage() );
    }
  }
}
