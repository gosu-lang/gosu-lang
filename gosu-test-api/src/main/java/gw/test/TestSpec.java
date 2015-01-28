/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.internal.ext.org.objectweb.asm.tree.MethodNode;
import gw.lang.UnstableAPI;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaBackedType;
import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.util.*;

@UnstableAPI
public class TestSpec implements Comparable<TestSpec> {
  private String _testType;
  private String[] _methods;

  public TestSpec(String type, String... methods) {
    _testType = type;
    _methods = methods;
  }

  public int compareTo(TestSpec o) {
    return this._testType.compareTo(o._testType);
  }

  public boolean runAllMethods() {
    return _methods == null || _methods.length == 0;
  }

  public String[] getMethods() {
    if (runAllMethods()) {
      return extractTestMethods(getTestType());
    } else {
      return _methods;
    }
  }

  public IType getTestType() {
    return TypeSystem.getByFullName(_testType);
  }

  public String getTestTypeName() {
    return _testType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TestSpec testSpec = (TestSpec) o;

    if (!Arrays.equals(_methods, testSpec._methods)) {
      return false;
    }
    if (_testType != null ? !_testType.equals(testSpec._testType) : testSpec._testType != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    result = (_testType != null ? _testType.hashCode() : 0);
    result = 31 * result + (_methods != null ? Arrays.hashCode(_methods) : 0);
    return result;
  }

  @SuppressWarnings("unchecked")
  public static String[] extractTestMethods(IType testType) {
    List<String> methodNames = new ArrayList<String>();
    if (testType instanceof IJavaBackedType) {
      methodNames = extractTestMethods(((IJavaBackedType) testType).getBackingClass());
    } else {
      List<? extends IMethodInfo> methodInfos = testType.getTypeInfo().getMethods();
      for (IMethodInfo methodInfo : methodInfos) {
        if (isTestMethod(methodInfo)) {
          methodNames.add(methodInfo.getDisplayName());
        }
      }
    }
    return methodNames.toArray(new String[methodNames.size()]);
  }

  public static List<String> extractTestMethods(Class<? extends TestCase> testClass) {
    Set<String> methodNames = new HashSet<String>();
    for (Method methodInfo : testClass.getMethods()) {
      if (TestSpec.isTestMethod(methodInfo) /*&& shouldIncludeTestMethod(methodInfo)*/) {
        methodNames.add(methodInfo.getName());
      }
    }
    return sortMethodsAccordingToSourceOrder(methodNames, testClass);
  }

  private static List<String> sortMethodsAccordingToSourceOrder(Set<String> testMethods, Class<? extends TestCase> clazz) {
    List<String> sortedMethods = new ArrayList<String>();
    for (MethodNode method : TestClassHelper.getMethodsSorted(clazz)) {
      if ((method.parameters == null || method.parameters.size() == 0) && testMethods.remove(method.name)) {
        sortedMethods.add(method.name);
      }
    }

    if (testMethods.size() != 0) {
      throw new IllegalArgumentException("Cannot find " + testMethods + " in the byte code of " + clazz.getName());
    }
    return sortedMethods;
  }

  static boolean isTestMethod(Method method) {
    return Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0 &&
        !Modifier.isStatic(method.getModifiers()) && method.getName().startsWith("test");
  }

  private static boolean isTestMethod(IMethodInfo methodInfo) {
    return methodInfo.isPublic() && methodInfo.getParameters().length == 0 &&
        !methodInfo.isStatic() && methodInfo.getName().startsWith("test");
  }
}