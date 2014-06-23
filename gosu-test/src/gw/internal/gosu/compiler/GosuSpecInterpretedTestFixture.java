/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 1, 2009
 * Time: 5:12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class GosuSpecInterpretedTestFixture implements GosuSpecTestFixture {
  @Override
  public Object newInstance(String typeName) {
    IType type = TypeSystem.getByFullName(typeName);
    return type.getTypeInfo().getCallableConstructor().getConstructor().newInstance();
  }

  @Override
  public Object invokeMethod(Object context, String methodName, Object... args) {
    IMethodInfo method = findUniqueMethodByName(TypeSystem.getFromObject(context), methodName);
    return method.getCallHandler().handleCall(context, args);
  }

  @Override
  public Object invokeStaticMethod(String typeName, String methodName, Object... args) {
    IMethodInfo method = findUniqueMethodByName(TypeSystem.getByFullName(typeName), methodName);
    return method.getCallHandler().handleCall(null, args);
  }

  private IMethodInfo findUniqueMethodByName(IType type, String methodName) {
    List<? extends IMethodInfo> methods = type.getTypeInfo().getMethods();
    List<IMethodInfo> matchingMethods = new ArrayList<IMethodInfo>();
    for (IMethodInfo method : methods) {
      if (method.getDisplayName().equals(methodName)) {
        matchingMethods.add(method);
      }
    }

    if (matchingMethods.size() == 0) {
      throw new IllegalArgumentException("No method named " + methodName + " was found on class " + type.getName());
    } else if (matchingMethods.size() == 1) {
      return matchingMethods.get(0);
    } else {
      throw new IllegalArgumentException("More than one method was found on " + type.getName() + " with name " + methodName + ".  This version cannot be used with overloaded methods.");
    }
  }

}
