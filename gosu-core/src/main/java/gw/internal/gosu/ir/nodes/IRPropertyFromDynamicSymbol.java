/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.parser.IReducedSymbol;
import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.ir.IRType;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;

public class IRPropertyFromDynamicSymbol extends IRFeatureBase implements IRProperty {

  private IReducedSymbol _symbol;

  public IRPropertyFromDynamicSymbol(IReducedSymbol symbol) {
    _symbol = symbol;
  }

  @Override
  public IRType getType() {
    return maybeReifyFieldType( _symbol.getGosuClass(), _symbol.getName(), _symbol.getType() );
  }

  @Override
  public IRType getAssignableType() {
    return getType();
  }

  @Override
  public String getName() {
    return resolveFieldName( _symbol.getGosuClass(), _symbol.getName() );
  }

  @Override
  public boolean isField() {
    return true;
  }

  @Override
  public boolean isCaptured() {
    return false;
  }

  @Override
  public IRMethod getGetterMethod() {
    throw new UnsupportedOperationException();
  }

  @Override
  public IRMethod getSetterMethod() {
    throw new UnsupportedOperationException();
  }

  @Override
  public IRType getOwningIRType() {
    return IRTypeResolver.getDescriptor( _symbol.getGosuClass() );
  }

  @Override
  public IType getOwningIType() {
    return _symbol.getGosuClass();
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forSymbol( _symbol );
  }

  @Override
  public boolean isStatic() {
    return _symbol.isStatic();
  }

  @Override
  public IRType getTargetRootIRType() {
    return getOwningIRType();
  }

  @Override
  public boolean isBytecodeProperty() {
    return true;
  }
}
