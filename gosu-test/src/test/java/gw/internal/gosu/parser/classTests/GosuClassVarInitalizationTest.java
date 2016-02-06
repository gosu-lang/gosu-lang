/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.ReflectUtil;
import gw.test.TestClass;

/**
 * User: dbrewster
 * Date: Apr 5, 2007
 * Time: 2:51:54 PM
 */
public class GosuClassVarInitalizationTest extends TestClass {

  public void testClassInitializesPrimitivesCorrectly() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.varTests.PrimitiveTest");
    Object instance = gosuClass.getTypeInfo().getConstructor().getConstructor().newInstance();
    assertEquals(false, ReflectUtil.getProperty( instance, "aBoolean" ));
    assertEquals((byte)0, ReflectUtil.getProperty( instance, "aByte" ));
    assertEquals((double)0, ReflectUtil.getProperty( instance, "aDouble" ));
    assertEquals((long)0, ReflectUtil.getProperty( instance, "aLong" ));
    assertEquals((short)0, ReflectUtil.getProperty( instance, "aShort" ));
    assertEquals(0, ReflectUtil.getProperty( instance, "anInteger" ));
    assertNull(null, ReflectUtil.getProperty( instance, "aBooleanObj" ));
  }
}
