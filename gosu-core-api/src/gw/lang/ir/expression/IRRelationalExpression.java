/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRRelationalExpression extends IRExpression {

  private IRExpression _lhs;
  private IRExpression _rhs;
  private Operation _op;

  public IRRelationalExpression(IRExpression lhs, IRExpression rhs, Operation op) {
    _lhs = lhs;
    _rhs = rhs;
    _op = op;

    lhs.setParent( this );
    rhs.setParent( this );
  }

  public enum Operation {
    GT, GTE, LT, LTE;
    
    public static Operation get( String strOp )
    {
      if( strOp.equals( ">" ) )
      {
        return IRRelationalExpression.Operation.GT;
      }
      else if (strOp.equals( ">=" ))
      {
        return IRRelationalExpression.Operation.GTE;
      }
      else if (strOp.equals( "<" ))
      {
        return IRRelationalExpression.Operation.LT;
      }
      else if (strOp.equals( "<=" ))
      {
        return IRRelationalExpression.Operation.LTE;
      }
      else
      {
        throw new IllegalArgumentException( "Unrecognized relational operation " + strOp );
      }
    }
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

  @Override
  public IRType getType() {
    return IRTypeConstants.pBOOLEAN();
  }
}
