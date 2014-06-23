/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.ir.IRType;
import gw.internal.gosu.ir.transform.util.NameResolver;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class IRMethodForDPSGetter extends IRFeatureBase implements IRMethod {

  private IRPropertyFromDynamicPropertySymbol _dps;

  public IRMethodForDPSGetter(IRPropertyFromDynamicPropertySymbol dps) {
    _dps = dps;
  }

  @Override
  public IRType getReturnType() {
    return _dps.getType();
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
    return NameResolver.getGetterNameForDPS( _dps.getDps() );
  }

  @Override
  public IRType getOwningIRType() {
    return _dps.getOwningIRType();
  }

  @Override
  public IType getOwningIType() {
    return _dps.getOwningIType();
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forSymbol( _dps.getDps().getGetterDfs() );
  }

  @Override
  public boolean isStatic() {
    return _dps.isStatic();
  }

  @Override
  public IRType getTargetRootIRType() {
    return _dps.getTargetRootIRType();
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables() {
    return null;
  }

  @Override
  public IFunctionType getFunctionType() {
    return (IFunctionType) _dps.getDps().getGetterDfs().getType();
  }

  @Override
  public boolean isBytecodeMethod() {
    return true;
  }

  @Override
  public boolean couldHaveTypeVariables() {
    return true;
  }

  @Override
  public boolean isGeneratedEnumMethod()
  {
    return false;
  }
}
