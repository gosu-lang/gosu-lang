/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IREqualityExpression extends IRExpression {
  private IRExpression _lhs;
  private IRExpression _rhs;
  private boolean _equals;

  public IREqualityExpression(IRExpression lhs, IRExpression rhs, boolean equals) {
    if (lhs.getType().isPrimitive()) {
      if (!lhs.getType().equals(rhs.getType())) {
        throw new IllegalArgumentException("Cannot build an equality expression between a primitive type and something other than the same type. LHS type is " + lhs.getType().getName() + " and RHS type is " + rhs.getType().getName());
      }
    } else if (rhs.getType().isPrimitive()) {
      throw new IllegalArgumentException("Cannot build an equality expression between an object and a primitive type. LHS type is " + lhs.getType().getName() + " and RHS type is " + rhs.getType().getName());
    }

    _lhs = lhs;
    _rhs = rhs;
    _equals = equals;

    _lhs.setParent( this );
    _rhs.setParent( this );
  }

  public IRExpression getLhs() {
    return _lhs;
  }

  public IRExpression getRhs() {
    return _rhs;
  }

  public boolean isEquals() {
    return _equals;
  }

  @Override
  public IRType getType() {
    return IRTypeConstants.pBOOLEAN();
  }
}
