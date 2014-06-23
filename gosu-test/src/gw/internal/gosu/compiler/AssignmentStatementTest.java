/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 */
public class AssignmentStatementTest extends ByteCodeTestBase
{
  public void testLocalVar() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj, "testLocalVar" );
    assertEquals( "Local", ret );
  }

  public void testParamVar() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj,  "testParamVar", "Param" );
    assertEquals( "Param", ret );
  }

  public void testInstanceVar() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj,  "testInstanceVar" );
    assertEquals( "Instance", ret );
  }

  public void testStaticVar() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj,  "testStaticVar" );
    assertEquals( "testStaticVar", ret );
  }

  public void testInstanceProperty() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj,  "testInstanceProperty" );
    assertEquals( "Instance", ret );
  }

  public void testStaticProperty() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj,  "testStaticProperty" );
    assertEquals( "testStaticProperty", ret );
  }

  public void testInstanceVarFromOuter() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj,  "testInstanceVarFromOuter" );
    assertEquals( "Instance", ret );
  }

  public void testInstancePropertyFromOuter() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj,  "testInstancePropertyFromOuter" );
    assertEquals( "Instance", ret );
  }

  public void testStaticVarFromOuter() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj,  "testStaticVarFromOuter" );
    assertEquals( "testStaticVarFromOuter", ret );
  }

  public void testStaticPropertyFromOuter() throws Exception
  {
    Object obj = newIdentifierTestClass();
    Object ret = invokeMethod( obj,  "testStaticPropertyFromOuter" );
    assertEquals( "testStaticPropertyFromOuter", ret );
  }

  private Object newIdentifierTestClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    return constructFromGosuClassloader( "gw.internal.gosu.compiler.sample.statement.classes.AssignmentStatementTestClass" );
  }
}