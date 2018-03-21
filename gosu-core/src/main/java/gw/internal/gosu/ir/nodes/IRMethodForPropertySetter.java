/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.ir.transform.util.NameResolver;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuPropertyInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.IJavaPropertyInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IRMethodForPropertySetter extends IRFeatureBase implements IRMethod {

  private IRPropertyFromPropertyInfo _owningProperty;

  public IRMethodForPropertySetter(IRPropertyFromPropertyInfo owningProperty) {
    _owningProperty = owningProperty;
  }

  @Override
  public IRType getReturnType() {
    IPropertyInfo pi = _owningProperty.getTerminalProperty();
    if( pi instanceof IJavaPropertyInfo ) {
      IJavaPropertyDescriptor propDesc = ((IJavaPropertyInfo)pi).getPropertyDescriptor();
      if( propDesc != null ) {
        IJavaClassMethod m = propDesc.getWriteMethod();
        if( m != null ) {
          // A setter is allowed to return the type of the property (e.g., to support builders)
          return IRTypeResolver.getDescriptor( m.getReturnType() );
        }
      }
    }
    return IRTypeConstants.pVOID();
  }

  @Override
  public List<IRType> getExplicitParameterTypes() {
    return Collections.singletonList( _owningProperty.getType() );
  }

  @Override
  public List<IRType> getAllParameterTypes() {
    List<IRType> paramTypes = new ArrayList<IRType>();
    addImplicitParameters(getOwningIType(), getFunctionType(), isStatic(), paramTypes);
    paramTypes.add( _owningProperty.getAssignableType() );
    return paramTypes;
  }

  @Override
  public String getName() {
    return NameResolver.getSetterName( _owningProperty.getTerminalProperty() );
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
      return (IFunctionType) ((IGosuPropertyInfo) _owningProperty.getTerminalProperty()).getDps().getSetterDfs().getType();
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
}