/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;

import java.util.List;

@UnstableAPI
public class IRNewExpression extends IRExpression {
  private IRType _ownersType;
  private List<IRType> _parameterTypes;
  private List<IRExpression> _args;

  public IRNewExpression(IRType ownersType, List<IRType> parameterTypes, List<IRExpression> args) {
    _ownersType = ownersType;
    _parameterTypes = maybeEraseStructuralTypes( ownersType, parameterTypes );
    _args = args;

    for (IRExpression arg : args) {
      arg.setParent( this );
    }
  }

  public IRType getOwnersType() {
    return _ownersType;
  }

  public List<IRType> getParameterTypes() {
    return _parameterTypes;
  }

  public List<IRExpression> getArgs() {
    return _args;
  }

  @Override
  public IRType getType() {
    return _ownersType;
  }
}
