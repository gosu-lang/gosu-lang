/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.tree.AbstractInsnNode;
import gw.internal.ext.org.objectweb.asm.tree.ClassNode;
import gw.internal.ext.org.objectweb.asm.tree.LineNumberNode;
import gw.internal.ext.org.objectweb.asm.tree.MethodNode;
import gw.util.StreamUtil;
import junit.framework.Test;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper methods for analyzing methods, and instantiating test classes.
 */
public class TestClassHelper {
  private static final Map<Class<?>, List<MethodNode>> cache = new ConcurrentHashMap<Class<?>, List<MethodNode>>();

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
  public static <T extends TestCase> List<MethodNode> getMethodsSorted(Class<T> clazz) {
    List<MethodNode> allMethods = cache.get(clazz);
    if (allMethods != null) {
      return allMethods;
    }

    ClassNode javaClass = parseClass(clazz);
    if (javaClass == null) {
      return Collections.emptyList();
    }
    List<MethodNode> currentClassMethods = javaClass.methods;
    Collections.sort(currentClassMethods, new Comparator<MethodNode>() {
      @Override
      public int compare(MethodNode o1, MethodNode o2) {
        return getLineNumber( o1 ) - getLineNumber( o2 );
      }
    });

    allMethods = new ArrayList<MethodNode>();
    // add method of super class first
    Class<? super T> superclass = clazz.getSuperclass();
    if (superclass != null && !superclass.getClass().equals(TestCase.class)) {
      allMethods.addAll(getMethodsSorted((Class<? extends TestCase>) superclass));
    }
    allMethods.addAll(currentClassMethods);

    cache.put(clazz, Collections.unmodifiableList(new ArrayList<MethodNode>(allMethods)));
    return allMethods;
  }

  private static int getLineNumber( MethodNode o2 ) {
    for( Iterator iter = o2.instructions.iterator(); iter.hasNext(); ) {
      AbstractInsnNode next = (AbstractInsnNode)iter.next();
      if( next instanceof LineNumberNode ) {
        return ((LineNumberNode)next).line;
      }
    }
    return -1;
  }

  private static ClassNode parseClass(Class<?> clazz) {
    String pathToClassFile = "/" + clazz.getName().replace('.', '/') + ".class";
    InputStream resourceAsStream = clazz.getResourceAsStream(pathToClassFile);
    if (resourceAsStream == null) {
      return null;
    }
    try {
      ClassNode jclass = new ClassNode( Opcodes.ASM5 );
      ClassReader cr = new ClassReader( resourceAsStream );
      cr.accept( jclass, 0 );
      return jclass;
    }
    catch( IOException e ) {
      throw new RuntimeException("Error during analyzing byte code of the class " + clazz.getName(), e);
    } finally {
      StreamUtil.closeNoThrow(resourceAsStream);
    }
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
