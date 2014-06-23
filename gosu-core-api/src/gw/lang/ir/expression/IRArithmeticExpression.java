/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRArithmeticExpression extends IRExpression {

  /**
   * The operation being performed.
   */
  public static enum Operation {
    Addition, Subtraction, Multiplication, Division, Remainder, ShiftLeft, ShiftRight, UnsignedShiftRight, BitwiseAnd, BitwiseOr, BitwiseXor
  }

  private IRType _type;
  private IRExpression _lhs;
  private IRExpression _rhs;
  private Operation _op;

  public IRArithmeticExpression(IRType type, IRExpression lhs, IRExpression rhs, Operation op) {
    _type = type;
    _lhs = lhs;
    _rhs = rhs;
    _op = op;

    lhs.setParent( this );
    rhs.setParent( this );
  }

  public IRType getType() {
    return _type;
  }

  public IRExpression getLhs() {
    return _lhs;
  }

  public IRExpression getRhs() {
    return _rhs;
  }

  public Operation getOp() {
    return _op;
  }
}
