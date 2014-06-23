/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;

import java.util.List;

@UnstableAPI
public class IRMethodCallExpression extends IRExpression {
  private String _name;
  private IRType _ownersType;
  private boolean _interface;
  private IRType _returnType;
  private List<IRType> _parameterTypes;
  private IRExpression _root;
  private List<IRExpression> _args;
  private boolean _isSpecial;
  private IRType _structureTypeOwner;

  public IRMethodCallExpression(String name, IRType ownersType, boolean isInterface, IRType returnType, List<IRType> parameterTypes, IRExpression root, List<IRExpression> args) {
    _name = name;
    _ownersType = ownersType;
    _interface = isInterface;
    _returnType = maybeEraseStructuralType( ownersType, returnType );
    _parameterTypes = maybeEraseStructuralTypes( ownersType, parameterTypes );
    _root = root;
    _args = args;

    if (root != null) {
      root.setParent( this );
    }
    for (IRExpression arg : args) {
      arg.setParent( this );
    }
  }

  public String getName() {
    return _name;
  }

  public IRType getOwnersType() {
    return _structureTypeOwner == null ? _ownersType : _structureTypeOwner;
  }

  public IRType getReturnType() {
    return _returnType;
  }

  public List<IRType> getParameterTypes() {
    return _parameterTypes;
  }

  public IRExpression getRoot() {
    return _root;
  }

  public List<IRExpression> getArgs() {
    return _args;
  }

  public boolean isInterface() {
    return _interface;
  }

  public boolean isSpecial() {
    return _isSpecial;
  }

  public void setSpecial(boolean special) {
    _isSpecial = special;
  }

  @Override
  public IRType getType() {
    return _returnType;
  }

  public void setStructuralTypeOwner( IRType gosuClassIRType ) {
    _structureTypeOwner = gosuClassIRType;
  }
}
