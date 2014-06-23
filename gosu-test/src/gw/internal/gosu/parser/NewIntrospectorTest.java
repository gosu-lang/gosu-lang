/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.test.TestClass;

import java.beans.IntrospectionException;
import java.beans.BeanInfo;

public class NewIntrospectorTest extends TestClass {
  
  public void testBooleanPropertiesAreOverriddenByNonBooleanProperties() throws IntrospectionException {
    GenericBeanInfo bi = NewIntrospector.getBeanInfo(Foo.class);
    assertEquals(1, bi.getPropertyDescriptors().length);
    assertEquals(String.class, bi.getPropertyDescriptors()[0].getPropertyType());

    bi = NewIntrospector.getBeanInfo(Foo2.class);
    assertEquals(1, bi.getPropertyDescriptors().length);
    assertEquals(String.class, bi.getPropertyDescriptors()[0].getPropertyType());
  }

  public void testGetterWithArgDoesNotCreateAPropertyDescriptor() throws IntrospectionException {
    GenericBeanInfo bi = NewIntrospector.getBeanInfo(Foo3.class);
    assertEquals(1, bi.getPropertyDescriptors().length);
    assertEquals(boolean.class, bi.getPropertyDescriptors()[0].getPropertyType());

    bi = NewIntrospector.getBeanInfo(Foo4.class);
    assertEquals(1, bi.getPropertyDescriptors().length);
    assertEquals(boolean.class, bi.getPropertyDescriptors()[0].getPropertyType());
  }

  interface Foo {
    public boolean isFoo();
    public String getFoo();
  }

  interface Foo2 {
    public String getFoo();
    public boolean isFoo();
  }

  interface Foo3 {
    public String getFoo(int s);
    public boolean isFoo();
  }

  interface Foo4 {
    public boolean isFoo();
    public String getFoo(int s);
  }
}
