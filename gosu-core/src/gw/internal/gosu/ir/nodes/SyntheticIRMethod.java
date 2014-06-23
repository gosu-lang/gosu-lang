/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.ir.IRType;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;

import java.util.List;

public class SyntheticIRMethod implements IRMethod {

  private IType _owner;
  private String _name;
  private IRType _returnType;
  private List<IRType> _parameterTypes;
  private IRelativeTypeInfo.Accessibility _accessibility;
  private boolean _static;

  public SyntheticIRMethod(IType owner, String name, IRType returnType, List<IRType> parameterTypes, IRelativeTypeInfo.Accessibility accessibility, boolean aStatic) {
    _owner = owner;
    _name = name;
    _returnType = returnType;
    _parameterTypes = parameterTypes;
    _accessibility = accessibility;
    _static = aStatic;
  }

  @Override
  public IRType getReturnType() {
    return _returnType;
  }

  @Override
  public List<IRType> getExplicitParameterTypes() {
    return _parameterTypes;
  }

  @Override
  public List<IRType> getAllParameterTypes() {
    return _parameterTypes;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public IRType getOwningIRType() {
    return IRTypeResolver.getDescriptor( _owner );
  }

  @Override
  public IType getOwningIType() {
    return _owner;
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return _accessibility;
  }

  @Override
  public boolean isStatic() {
    return _static;
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
    return true;
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
