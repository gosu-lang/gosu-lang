/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;
import junit.framework.Assert;

import java.net.URI;

public class LazyTypeTest extends TestClass {

  public void testWithSimpleType() {
    assertEquals(JavaTypes.STRING(), new LazyType("String").get());
  }

  public void testWithMissingSimpleType() {
    assertEquals(TypeSystem.getErrorType(), new LazyType("String2").get());
  }

  public void testWithArrayType() {
    assertEquals(JavaTypes.STRING().getArrayType(), new LazyType("String[]").get());
  }

  public void testWithMissingArrayType() {
    assertEquals(TypeSystem.getErrorType(), new LazyType("String2[]").get());
  }

  public void testWithDoubleArrayType() {
    assertEquals(JavaTypes.STRING().getArrayType().getArrayType(), new LazyType("String[][]").get());
  }

  public void testWithDoubleMissingArrayType() {
    assertEquals(TypeSystem.getErrorType(), new LazyType("String2[][]").get());
  }

}
