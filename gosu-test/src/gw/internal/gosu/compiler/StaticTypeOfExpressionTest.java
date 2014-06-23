/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

/**
 */
public class StaticTypeOfExpressionTest extends ByteCodeTestBase
{
  public void testShiftRightInt() throws Exception
  {
    Object obj = newTestStatictypeofExpression();
    Object val = invokeMethod( obj,  "testSimple" );
    IType ret = (IType)val;
    assertEquals( TypeSystem.getByFullName( "gw.internal.gosu.compiler.sample.expression.TestStaticTypeofExpression" ), ret );
  }

  private Object newTestStatictypeofExpression() throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    final String cls = "gw.internal.gosu.compiler.sample.expression.TestStaticTypeofExpression";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    assertNotNull( javaClass );
    assertEquals( cls, javaClass.getName() );
    assertNotNull( javaClass.newInstance() );
    return javaClass.newInstance();
  }
}