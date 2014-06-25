/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRNewArrayExpression extends IRExpression {
  private IRType _componentType;
  private IRExpression _sizeExpression;

  public IRNewArrayExpression(IRType componentType, IRExpression sizeExpression) {
    _componentType = maybeEraseStructuralType( componentType );
    _sizeExpression = sizeExpression;

    sizeExpression.setParent( this );
  }

  public IRType getComponentType() {
    return _componentType;
  }

  public IRType getType() {
    return _componentType.getArrayType();
  }

  public IRExpression getSizeExpression() {
    return _sizeExpression;
  }
}
