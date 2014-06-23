/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRFieldGetExpression extends IRExpression {
  private IRExpression _lhs;
  private String _name;
  private IRType _fieldType;
  private IRType _ownersType;

  public IRFieldGetExpression(IRExpression lhs, String name, IRType fieldType, IRType ownersType) {
    _lhs = lhs;
    _name = name;
    _fieldType = maybeEraseStructuralType( ownersType, fieldType );
    _ownersType = ownersType;

    if (lhs != null) {
      lhs.setParent( this );
    }
  }

  public IRExpression getLhs() {
    return _lhs;
  }

  public String getName() {
    return _name;
  }

  public IRType getFieldType() {
    return _fieldType;
  }

  public IRType getOwnersType() {
    return _ownersType;
  }

  @Override
  public IRType getType() {
    return _fieldType;
  }
}