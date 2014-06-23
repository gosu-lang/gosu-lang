/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 1, 2009
 * Time: 4:43:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class GosuSpecBytecodeTestFixture implements GosuSpecTestFixture {
  @Override
  public Object newInstance(String typeName) {
    try {
      Class<?> javaClass = findClassByName(typeName);
      Object result = javaClass.newInstance();
      if (result == null) {
        throw new IllegalStateException("Calling newInstance on class " + typeName + " returned null");
      }
      return result;
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object invokeMethod(Object context, String methodName, Object... args) {
    Method method = findUniqueMethodByName(context.getClass(), methodName);
    try {
      return method.invoke(context, args);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object invokeStaticMethod(String typeName, String methodName, Object... args) {
    Class cls = findClassByName(typeName);
    Method method = findUniqueMethodByName(cls, methodName);
    try {
      return method.invoke(null, args);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private Method findUniqueMethodByName(Class cls, String methodName) {
    Method[] methods = cls.getMethods();
    List<Method> matchingMethods = new ArrayList<Method>();
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        matchingMethods.add(method);
      }
    }

    if (matchingMethods.size() == 0) {
      throw new IllegalArgumentException("No method named " + methodName + " was found on class " + cls.getName());
    } else if (matchingMethods.size() == 1) {
      return matchingMethods.get(0);
    } else {
      throw new IllegalArgumentException("More than one method was found on " + cls.getName() + " with name " + methodName + ".  This version cannot be used with overloaded methods.");
    }
  }

  private Class findClassByName(String typeName) {
    try {
      Class<?> javaClass = GosuClassLoader.instance().findClass(typeName);
      if (javaClass == null) {
        throw new IllegalArgumentException("No class found named " + typeName);
      }
      if (!javaClass.getName().equals(typeName)) {
        throw new IllegalStateException("Class loaded from name " + typeName + " reports having name " + javaClass.getName());
      }
      return javaClass;
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
