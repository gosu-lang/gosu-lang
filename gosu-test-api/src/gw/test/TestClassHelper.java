/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.util.StreamUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.Method;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper methods for analyzing methods, and instantiating test classes.
 */
public class TestClassHelper {
  private static final Map<Class<?>, List<Method>> cache = new ConcurrentHashMap<Class<?>, List<Method>>();

  /**
   * Returns list of methods according to their order in the source file.
   * <p/>
   * Supertype methods go first in the list.
   * <p/>
   * Returns empty list if cannot find class file for the specified class. Class file is retrieved by
   * using {@link Class#getResourceAsStream} so it won't work for classes generated at runtine.
   *
   * @param clazz class to analyze
   * @return list of method names
   */
  @SuppressWarnings("unchecked")
  public static <T extends TestCase> List<Method> getMethodsSorted(Class<T> clazz) {
    List<Method> allMethods = cache.get(clazz);
    if (allMethods != null) {
      return allMethods;
    }

    JavaClass javaClass = parseClass(clazz);
    if (javaClass == null) {
      return Collections.emptyList();
    }
    List<Method> currentClassMethods = Arrays.asList(javaClass.getMethods());
    Collections.sort(currentClassMethods, new Comparator<Method>() {
      @Override
      public int compare(Method o1, Method o2) {
        return getMethodLineNumber(o1).compareTo(getMethodLineNumber(o2));
      }
    });

    allMethods = new ArrayList<Method>();
    // add method of super class first
    Class<? super T> superclass = clazz.getSuperclass();
    if (superclass != null && !superclass.getClass().equals(TestCase.class)) {
      allMethods.addAll(getMethodsSorted((Class<? extends TestCase>) superclass));
    }
    allMethods.addAll(currentClassMethods);

    cache.put(clazz, Collections.unmodifiableList(new ArrayList<Method>(allMethods)));
    return allMethods;
  }

  private static JavaClass parseClass(Class<?> clazz) {
    String pathToClassFile = "/" + clazz.getName().replace('.', '/') + ".class";
    InputStream resourceAsStream = clazz.getResourceAsStream(pathToClassFile);
    if (resourceAsStream == null) {
      return null;
    }
    try {
      return new ClassParser(resourceAsStream, pathToClassFile).parse();
    } catch (IOException e) {
      throw new RuntimeException("Error during analyzing byte code of the class " + clazz.getName(), e);
    } finally {
      StreamUtil.closeNoThrow(resourceAsStream);
    }
  }

  private static Integer getMethodLineNumber(Method method) {
    LineNumberTable lineNumberTable = method.getLineNumberTable();
    return lineNumberTable == null ? -1 : lineNumberTable.getLineNumberTable()[0].getLineNumber();
  }

  public static <T extends TestCase> Test createTestSuite(Class<T> clazz, Iterable<String> methodNames) {
    try {
      Constructor<T> constructor = getConstructor(clazz);
      junit.framework.TestSuite newSuite = new junit.framework.TestSuite();
      for (String name : methodNames) {
        TestCase test;
        if (constructor.getParameterTypes().length == 0) {
          test = constructor.newInstance();
          test.setName(name);
        } else {
          test = constructor.newInstance(name);
        }
        newSuite.addTest(test);
      }
      return newSuite;
    } catch (InstantiationException e) {
      throw new RuntimeException("Cannot instantiate test class " + clazz.getName(), e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(clazz.getName() + " constructor is not accessible", e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(clazz.getName() + "(String name) constructor threw an exception", e);
    }
  }

  private static <T extends TestCase> Constructor<T> getConstructor(Class<T> clazz) {
    try {
      Constructor<T> constructor = clazz.getConstructor(String.class);
      if (Modifier.isPublic(constructor.getModifiers())) {
        return constructor;
      }
    } catch (NoSuchMethodException e) {
    }

    try {
      Constructor<T> constructor = clazz.getConstructor();
      if (Modifier.isPublic(constructor.getModifiers())) {
        return constructor;
      }
    } catch (NoSuchMethodException e) {
    }

    throw new RuntimeException("Did not find public " + clazz.getName() + "(String name) or " + clazz.getName()
            + "() constructor");
  }
}
