/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.parser.IReducedSymbol;
import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.ir.IRType;
import gw.internal.gosu.parser.CapturedSymbol;
import gw.internal.gosu.parser.ICompilableTypeInternal;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;

public class IRPropertyFromCapturedSymbol extends IRFeatureBase implements IRProperty {

  private ICompilableTypeInternal _owningType;
  private IReducedSymbol _symbol;

  public IRPropertyFromCapturedSymbol( ICompilableTypeInternal owningType, IReducedSymbol symbol) {
    _owningType = owningType;
    _symbol = symbol;
  }

  @Override
  public IRType getType() {
    return maybeReifyFieldType( _owningType, _symbol.getName(), _symbol.getType() ).getArrayType();
  }

  @Override
  public String getName() {
    return AbstractElementTransformer.CAPTURED_VAR_PREFIX + _symbol.getName();
  }

  @Override
  public boolean isField() {
    return true;
  }

  @Override
  public boolean isCaptured() {
    return true;
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
    return IRTypeResolver.getDescriptor( _owningType );
  }

  @Override
  public IType getOwningIType() {
    return _owningType;
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forCapturedVar();
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