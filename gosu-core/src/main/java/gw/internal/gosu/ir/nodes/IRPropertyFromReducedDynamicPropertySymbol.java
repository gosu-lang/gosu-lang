/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.parser.*;
import gw.lang.ir.IRType;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IReducedDynamicPropertySymbol;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;

public class IRPropertyFromReducedDynamicPropertySymbol implements IRProperty {

  private ReducedDynamicPropertySymbol _rdps;

  public IRPropertyFromReducedDynamicPropertySymbol(ReducedDynamicPropertySymbol rdps) {
    _rdps = rdps;
  }

  public ReducedDynamicPropertySymbol getDps() {
    return _rdps;
  }

  @Override
  public IRType getType() {
    return IRTypeResolver.getDescriptor( getBoundedPropertyType(_rdps) );
  }

  @Override
  public String getName() {
    return _rdps.getName();
  }

  @Override
  public boolean isField() {
    return false;
  }

  @Override
  public boolean isCaptured() {
    return false;
  }

  @Override
  public IRMethod getGetterMethod() {
    return new IRMethodForReducedDPSGetter( this );
  }

  @Override
  public IRMethod getSetterMethod() {
    return new IRMethodForReducedDPSSetter( this );
  }

  @Override
  public IRType getOwningIRType() {
    return IRTypeResolver.getDescriptor( _rdps.getGosuClass() );
  }

  @Override
  public IType getOwningIType() {
    return _rdps.getGosuClass();
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forSymbol(_rdps);
  }

  @Override
  public boolean isStatic() {
    return _rdps.isStatic();
  }

  @Override
  public IRType getTargetRootIRType() {
    return getOwningIRType();
  }

  @Override
  public boolean isBytecodeProperty() {
    return true;
  }

  public IType getBoundedPropertyType( IDynamicPropertySymbol dps )
  {
    while( dps instanceof ParameterizedDynamicPropertySymbol)
    {
      ParameterizedDynamicPropertySymbol pdfs = (ParameterizedDynamicPropertySymbol)dps;
      dps = pdfs.getDelegate();
    }
    return TypeLord.getDefaultParameterizedTypeWithTypeVars( dps.getType() );
  }

  public IType getBoundedPropertyType( IReducedDynamicPropertySymbol rdps )
  {
    while( rdps instanceof ReducedParameterizedDynamicPropertySymbol)
    {
      ReducedParameterizedDynamicPropertySymbol pdfs = (ReducedParameterizedDynamicPropertySymbol)rdps;
      rdps = pdfs.getDelegate();
    }
    return TypeLord.getDefaultParameterizedTypeWithTypeVars( rdps.getType() );
  }
}
