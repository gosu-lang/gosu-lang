/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.config.CommonServices;
import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.ir.IRType;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.ArrayList;

public class IRMethodFromMethod implements IRMethod {
  private Method _method;

  public IRMethodFromMethod(Method method) {
    _method = method;
  }

  @Override
  public IRType getReturnType() {
    return IRTypeResolver.getDescriptor(_method.getReturnType());
  }

  @Override
  public List<IRType> getExplicitParameterTypes() {
    List<IRType> paramTypes = new ArrayList<IRType>();
    for (Class<?> paramClass : _method.getParameterTypes()) {
      paramTypes.add(IRTypeResolver.getDescriptor(paramClass));
    }
    return paramTypes;
  }

  @Override
  public List<IRType> getAllParameterTypes() {
    return getExplicitParameterTypes();
  }

  @Override
  public String getName() {
    return _method.getName();
  }

  @Override
  public IRType getOwningIRType() {
    return IRTypeResolver.getDescriptor(_method.getDeclaringClass());
  }

  @Override
  public IType getOwningIType() {
    return TypeSystem.get(_method.getDeclaringClass(), TypeSystem.getGlobalModule());
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forMethod(_method);
  }

  @Override
  public boolean isStatic() {
    return Modifier.isStatic(_method.getModifiers());
  }

  @Override
  public IRType getTargetRootIRType() {
    return getOwningIRType();
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables() {
    return null;
  }

  @Override
  public IFunctionType getFunctionType() {
    return null;
  }

  @Override
  public boolean isBytecodeMethod() {
    return !CommonServices.getEntityAccess().isExternal( _method.getDeclaringClass() );
  }

  @Override
  public boolean couldHaveTypeVariables() {
    return false;
  }

  @Override
  public boolean isGeneratedEnumMethod()
  {
    return false;
  }
}
