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
    Addition("+"),
    Subtraction("-"),
    Multiplication("*"),
    Division("/"),
    Remainder("%"),
    ShiftLeft("<<"),
    ShiftRight(">>"),
    UnsignedShiftRight(">>>"),
    BitwiseAnd("&"),
    BitwiseOr("|"),
    BitwiseXor("^");
    private String _op;

    Operation( String op ) {
      _op = op;
    }

    public static Operation fromString( String op ) {
      if( op.charAt( 0 ) == '?' ) {
        op = op.substring( 1 );
      }
      if( op.equals( "+" ) ) {
        return Addition;
      }
      if( op.equals( "-" ) ) {
        return Subtraction;
      }
      if( op.equals( "*" ) ) {
        return Multiplication;
      }
      if( op.equals( "/" ) ) {
        return Division;
      }
      if( op.equals( "%" ) ) {
        return Remainder;
      }
      if( op.equals( "<<" ) ) {
        return ShiftLeft;
      }
      if( op.equals( ">>" ) ) {
        return ShiftRight;
      }
      if( op.equals( ">>>" ) ) {
        return UnsignedShiftRight;
      }
      if( op.equals( "&" ) ) {
        return BitwiseAnd;
      }
      if( op.equals( "|" ) ) {
        return BitwiseOr;
      }
      if( op.equals( "^" ) ) {
        return BitwiseXor;
      }
      throw new IllegalStateException( "Undefined operator: " + op );
    }
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
