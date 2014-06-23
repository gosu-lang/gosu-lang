/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.awt.*;

/**
 */
public class MiscClassTest extends ByteCodeTestBase
{
  public void testImplicitCovarianceFromInheritiedMethodOnGenericBaseClass() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.classes.GosuShape";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    Object obj = javaClass.newInstance();

    Rectangle rc = new Rectangle();
    Rectangle val = (Rectangle)invokeMethod( obj, "foo", rc );
    assertEquals( rc, val );
  }

  public void testImplicitCovarianceFromInheritiedGenericMethodOnGenericBaseClass() throws Exception
  {
    final String cls = "gw.internal.gosu.compiler.sample.statement.classes.GosuShape";
    Class<?> javaClass = GosuClassLoader.instance().findClass( cls );
    Object obj = javaClass.newInstance();

    Rectangle rc = new Rectangle();
    Rectangle val = (Rectangle)invokeMethod( obj, "bar", rc );
    assertEquals( rc, val );
  }
}