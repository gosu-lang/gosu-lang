/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;

import java.net.URI;

import junit.framework.Assert;

public class TypeSystemTest extends TestClass {

  public void testParseTypeLiteral() {
    Assert.assertEquals(JavaTypes.STRING(), TypeSystem.parseTypeLiteral("String"));
    Assert.assertEquals(TypeSystem.getDefaultParameterizedType(JavaTypes.LIST()), TypeSystem.parseTypeLiteral("List"));
    Assert.assertEquals(TypeSystem.get(URI.class), TypeSystem.parseTypeLiteral("java.net.URI"));
    try {
      TypeSystem.parseTypeLiteral("URI"); // no package name
      Assert.fail("Expected RuntimeException");
    }
    catch (RuntimeException ex) {
      // good
    }
  }

}
