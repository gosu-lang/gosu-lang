/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRType;

@UnstableAPI
public class IRFieldSetStatement extends IRStatement {
  private IRExpression _lhs;
  private IRExpression _rhs;
  private String _name;
  private IRType _fieldType;
  private IRType _ownersType;

  public IRFieldSetStatement(IRExpression lhs, IRExpression rhs, String name, IRType fieldType, IRType ownersType) {
    _lhs = lhs;
    _rhs = rhs;
    _name = name;
    _fieldType = maybeEraseStructuralType( ownersType, fieldType );
    _ownersType = ownersType;

    if (lhs != null) {
      lhs.setParent( this );
    }
    rhs.setParent( this);
  }

  public IRExpression getLhs() {
    return _lhs;
  }

  public IRExpression getRhs() {
    return _rhs;
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
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }
}
