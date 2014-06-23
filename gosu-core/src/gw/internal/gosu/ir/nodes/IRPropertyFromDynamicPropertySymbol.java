/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.ir.IRType;
import gw.internal.gosu.parser.DynamicPropertySymbol;
import gw.internal.gosu.parser.ParameterizedDynamicPropertySymbol;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;

public class IRPropertyFromDynamicPropertySymbol implements IRProperty {

  private DynamicPropertySymbol _dps;

  public IRPropertyFromDynamicPropertySymbol(DynamicPropertySymbol dps) {
    _dps = dps;
  }

  public DynamicPropertySymbol getDps() {
    return _dps;
  }

  @Override
  public IRType getType() {
    return IRTypeResolver.getDescriptor( getBoundedPropertyType( _dps ) );
  }

  @Override
  public String getName() {
    return _dps.getName();
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
    return new IRMethodForDPSGetter( this );
  }

  @Override
  public IRMethod getSetterMethod() {
    return new IRMethodForDPSSetter( this );
  }

  @Override
  public IRType getOwningIRType() {
    return IRTypeResolver.getDescriptor( _dps.getGosuClass() );
  }

  @Override
  public IType getOwningIType() {
    return _dps.getGosuClass();
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forSymbol( _dps );
  }

  @Override
  public boolean isStatic() {
    return _dps.isStatic();
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
}
