/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests.gwtest;

/**
 */
public class JavaConstructorsClass {
  public static class PrivateConstructorClass {
    private PrivateConstructorClass() {}
  }

  public static class ProtectedConstructorClass {
    protected ProtectedConstructorClass() {}
  }

  public static class SimpleClass {
    public SimpleClass() {}
  }

  public static class SubClass extends SimpleClass {
    public SubClass() {}
  }

  public abstract static class AbstractClassForJavaConstructorsTest {
    public AbstractClassForJavaConstructorsTest(String var) {}
  }
}
