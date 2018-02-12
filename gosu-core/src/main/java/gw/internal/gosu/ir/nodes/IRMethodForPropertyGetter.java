/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.reflect.IAnnotatedFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuPropertyInfo;
import gw.lang.ir.IRType;
import gw.internal.gosu.ir.transform.util.NameResolver;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class IRMethodForPropertyGetter extends IRFeatureBase implements IRMethod {

  private IRPropertyFromPropertyInfo _owningProperty;

  public IRMethodForPropertyGetter(IRPropertyFromPropertyInfo owningProperty) {
    _owningProperty = owningProperty;
  }

  @Override
  public IRType getReturnType() {
    return _owningProperty.getType();
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
    return NameResolver.getGetterName( _owningProperty.getTerminalProperty() );
  }

  @Override
  public IRType getOwningIRType() {
    return _owningProperty.getOwningIRType();
  }

  @Override
  public IType getOwningIType() {
    return _owningProperty.getOwningIType();
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return _owningProperty.getAccessibility();
  }

  @Override
  public boolean isStatic() {
    return _owningProperty.isStatic();
  }

  @Override
  public IRType getTargetRootIRType() {
    return _owningProperty.getTargetRootIRType();
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables() {
    return null;
  }

  @Override
  public IFunctionType getFunctionType() {
    if (_owningProperty.getTerminalProperty() instanceof IGosuPropertyInfo) {
      return (IFunctionType) ((IGosuPropertyInfo) _owningProperty.getTerminalProperty()).getDps().getGetterDfs().getType();
    } else {
      return null;
    }
  }

  @Override
  public boolean isBytecodeMethod() {
    return _owningProperty.isBytecodeProperty();
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

  @Override
  public IAnnotatedFeatureInfo getFeatureInfo()
  {
    return _owningProperty.getTerminalProperty();
  }
}
