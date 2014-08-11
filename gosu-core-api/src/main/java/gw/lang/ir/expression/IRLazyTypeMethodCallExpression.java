/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;


@UnstableAPI
public class IRLazyTypeMethodCallExpression extends IRExpression {
  private String _name;
  private IRType _ownersType;
  private int _iFunctionTypeParamCount;
  private boolean _bStatic;


  public IRLazyTypeMethodCallExpression( String name, IRType ownersType, int iFunctionTypeParamCount, boolean bStatic ) {
    _name = name;
    _ownersType = ownersType;
    _iFunctionTypeParamCount = iFunctionTypeParamCount;
    _bStatic = bStatic;
  }

  public String getName() {
    return _name;
  }

  public IRType getOwnersType() {
    return _ownersType;
  }

  @Override
  public IRType getType() {
    return null;
  }

  public int getFunctionTypeParamCount() {
    return _iFunctionTypeParamCount;
  }

  public boolean isStatic() {
    return _bStatic;
  }
}
