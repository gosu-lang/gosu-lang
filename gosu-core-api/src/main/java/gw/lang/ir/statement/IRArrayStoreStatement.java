/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRArrayStoreStatement extends IRStatement {
  private IRExpression _target;
  private IRExpression _index;
  private IRExpression _value;
  private IRType _componentType;

  public IRArrayStoreStatement(IRExpression target, IRExpression index, IRExpression value, IRType componentType) {
    _target = target;
    _index = index;
    _value = value;
    _componentType = maybeEraseStructuralType( componentType );

    target.setParent( this );
    index.setParent( this );
    value.setParent( this );
  }
  
  public IRExpression getTarget() {
    return _target;
  }

  public IRExpression getIndex() {
    return _index;
  }

  public IRExpression getValue() {
    return _value;
  }

  public IRType getComponentType() {
    return _componentType;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }
}
