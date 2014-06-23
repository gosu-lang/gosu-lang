/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Mar 2, 2010
 * Time: 1:05:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class HasInnerClassWithSameNameAsStaticMethod {

  public static SomeInnerClass someInnerClass(String arg) {
    return new SomeInnerClass();
  }

  public static class SomeInnerClass {

  }
}
