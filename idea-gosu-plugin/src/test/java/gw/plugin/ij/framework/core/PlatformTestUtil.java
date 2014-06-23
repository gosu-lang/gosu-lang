/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.core;

import gw.testharness.Disabled;
import gw.testharness.KnownBreak;
import junit.framework.Assert;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class PlatformTestUtil {
  private static Method getAndValidateMethod(Class<?> testClass, String testName) {
    Assert.assertNotNull("TestCase.fName cannot be null", testName);
    Method runMethod = null;
    try {
      // use getMethod to get all public inherited
      // methods. getDeclaredMethods returns all
      // methods of this class but excludes the
      // inherited ones.
      runMethod = testClass.getMethod(testName, (Class[]) null);
    } catch (NoSuchMethodException e) {
      Assert.fail("Method \"" + testName + "\" not found");
    }
    if (!Modifier.isPublic(runMethod.getModifiers())) {
      Assert.fail("Method \"" + testName + "\" should be public");
    }
    return runMethod;
  }


  public static boolean canRunTest(Class<?> testClass, String testName) {
    Method method = getAndValidateMethod(testClass, testName);
    if (method.getAnnotation(KnownBreak.class) != null || method.getAnnotation(Disabled.class) != null) {
      return false;
    }
    return true;
  }
}
