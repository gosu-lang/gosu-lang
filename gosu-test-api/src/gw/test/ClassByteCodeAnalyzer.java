/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.util.StreamUtil;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.Method;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Analyzes byte code of Java classes.
 */
public class ClassByteCodeAnalyzer {
  private final Map<Class<?>, List<Method>> cache = new ConcurrentHashMap<Class<?>, List<Method>>();

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
  public List<Method> getMethodsSorted(Class<?> clazz) {
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
    Class<?> superclass = clazz.getSuperclass();
    if (superclass != null) {
      allMethods.addAll(getMethodsSorted(superclass));
    }
    allMethods.addAll(currentClassMethods);

    cache.put(clazz, Collections.unmodifiableList(new ArrayList<Method>(allMethods)));
    return allMethods;
  }

  private JavaClass parseClass(Class<?> clazz) {
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

}
