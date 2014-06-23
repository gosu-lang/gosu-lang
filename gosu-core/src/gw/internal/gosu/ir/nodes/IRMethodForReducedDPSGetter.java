/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.NameResolver;
import gw.lang.ir.IRType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGenericTypeVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IRMethodForReducedDPSGetter extends IRFeatureBase implements IRMethod {

  private IRPropertyFromReducedDynamicPropertySymbol _rdps;

  public IRMethodForReducedDPSGetter(IRPropertyFromReducedDynamicPropertySymbol rdps) {
    _rdps = rdps;
  }

  @Override
  public IRType getReturnType() {
    return _rdps.getType();
  }

  @Override
  public List<IRType> getExplicitParameterTypes() {
    return Collections.emptyList();
  }

  @Override
  public List<IRType> getAllParameterTypes() {
    List<IRType> paramTypes = new ArrayList<IRType>();
    addImplicitParameters(getOwningIType(), getFunctionType(), isStatic(), paramTypes);
    return paramTypes;
  }

  @Override
  public String getName() {
    return NameResolver.getGetterNameForDPS( _rdps.getDps() );
  }

  @Override
  public IRType getOwningIRType() {
    return _rdps.getOwningIRType();
  }

  @Override
  public IType getOwningIType() {
    return _rdps.getOwningIType();
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forSymbol( _rdps.getDps().getGetterDfs() );
  }

  @Override
  public boolean isStatic() {
    return _rdps.isStatic();
  }

  @Override
  public IRType getTargetRootIRType() {
    return _rdps.getTargetRootIRType();
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables() {
    return null;
  }

  @Override
  public IFunctionType getFunctionType() {
    return (IFunctionType) _rdps.getDps().getGetterDfs().getType();
  }

  @Override
  public boolean isGeneratedEnumMethod() {
    return false;
  }

  @Override
  public boolean isBytecodeMethod() {
    return true;
  }

  @Override
  public boolean couldHaveTypeVariables() {
    return true;
  }
}
