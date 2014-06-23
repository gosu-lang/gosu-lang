/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.testharness.InProgress;
import gw.testharness.KnownBreak;

public class TemplateStringLiteralTest extends ByteCodeTestBase
{
  public void testSimpleExpr1()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr1" );
    assertEquals( "42", result );
  }

  public void testSimpleExpr2()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr2" );
    assertEquals( "true", result );
  }

  public void testSimpleExpr3()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr3" );
    assertEquals( "test", result );
  }

  public void testSimpleExpr4()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr4" );
    assertEquals( "java.lang.String", result );
  }

  public void testSimpleExpr5()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr5" );
    assertEquals( "42", result );
  }

  public void testSimpleExpr6()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr6" );
    assertEquals( "testtest", result );
  }

  public void testSimpleExpr7()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr7" );
    assertEquals( "test", result );
  }

  public void testLocalVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_localVar" );
    assertEquals( "localVar", result );
  }

  public void testClassVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_classVar" );
    assertEquals( "classVar", result );
  }

  public void testThisClassVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisClassVar" );
    assertEquals( "classVar", result );
  }

  public void testStaticVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_staticVar" );
    assertEquals( "staticVar", result );
  }

  public void testThisStaticVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisStaticVar" );
    assertEquals( "staticVar", result );
  }

  public void testClassPropCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_classProp" );
    assertEquals( "classProp", result );
  }


  public void testThisClassPropCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisClassProp" );
    assertEquals( "classProp", result );
  }

  public void testStaticPropCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_staticProp" );
    assertEquals( "staticProp", result );
  }

  @InProgress
  public void testThisStaticPropCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisStaticProp" );
    assertEquals( "staticProp", result );
  }

  public void testClassFuncCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_classFunc" );
    assertEquals( "classFunc", result );
  }


  public void testThisClassFuncCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisClassFunc" );
    assertEquals( "classFunc", result );
  }

  public void testStaticFuncCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_staticFunc" );
    assertEquals( "staticFunc", result );
  }

  public void testThisStaticFuncCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisStaticFunc" );
    assertEquals( "staticFunc", result );
  }

  public void testBlockCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_blockExample" );
    assertEquals( "block", result );
  }

  public void testNestedSimpleExpr1()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr1" );
    assertEquals( "42", result );
  }

  public void testNestedSimpleExpr2()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr2" );
    assertEquals( "true", result );
  }

  public void testNestedSimpleExpr3()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr3" );
    assertEquals( "test", result );
  }

  public void testNestedSimpleExpr4()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr4" );
    assertEquals( "java.lang.String", result );
  }

  public void testNestedSimpleExpr5()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr5" );
    assertEquals( "42", result );
  }

  public void testNestedSimpleExpr6()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr6" );
    assertEquals( "testtest", result );
  }

  public void testNestedSimpleExpr7()
  {
    Object result = invokeMethod( makeObject(), "nested_simpleExpr7" );
    assertEquals( "test", result );
  }

  public void testNestedLocalVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_localVar" );
    assertEquals( "localVar", result );
  }

  public void testNestedClassVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_classVar" );
    assertEquals( "classVar", result );
  }

  public void testNestedThisClassVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisClassVar" );
    assertEquals( "classVar", result );
  }

  public void testNestedStaticVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_staticVar" );
    assertEquals( "staticVar", result );
  }

  public void testNestedThisStaticVarCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisStaticVar" );
    assertEquals( "staticVar", result );
  }

  public void testNestedClassPropCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_classProp" );
    assertEquals( "classProp", result );
  }


  public void testNestedThisClassPropCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisClassProp" );
    assertEquals( "classProp", result );
  }

  public void testNestedStaticPropCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_staticProp" );
    assertEquals( "staticProp", result );
  }

  @InProgress
  public void testNestedThisStaticPropCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisStaticProp" );
    assertEquals( "staticProp", result );
  }

  public void testNestedClassFuncCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_classFunc" );
    assertEquals( "classFunc", result );
  }


  public void testNestedThisClassFuncCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisClassFunc" );
    assertEquals( "classFunc", result );
  }

  public void testNestedStaticFuncCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_staticFunc" );
    assertEquals( "staticFunc", result );
  }

  public void testNestedThisStaticFuncCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_thisStaticFunc" );
    assertEquals( "staticFunc", result );
  }

  public void testNestedBlockCapture()
  {
    Object result = invokeMethod( makeObject(), "nested_blockExample" );
    assertEquals( "block", result );
  }

//  public void testNested1()
//  {
//    Object result = invokeMethod( makeObject(), "nested1" );
//    assertEquals( "testtest", result );
//  }

  private Object makeObject()
  {
    return constructFromGosuClassloader( "gw.internal.gosu.compiler.sample.expression.TemplateStrings" );
  }
}