/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;

public class DefaultArrayTypeTest extends TestClass
{
  public void testDefaultArrayTypesArrayTypeIsCorrectlyFormed()
  {
    DefaultArrayType type = new DefaultArrayType( JavaTypes.STRING(), JavaTypes.STRING().getBackingClassInfo(), JavaTypes.STRING().getTypeLoader() );
    assertEquals( TypeSystem.getJavaClassInfo(String[][].class), type.getArrayType().getConcreteClass() );
    assertTrue( type.makeArrayInstance( 0 ) instanceof String[][] );
  }
}