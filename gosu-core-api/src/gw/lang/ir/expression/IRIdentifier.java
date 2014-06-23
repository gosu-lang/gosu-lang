/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRSymbol;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRIdentifier extends IRExpression {
  private IRSymbol _symbol;

  public IRIdentifier(IRSymbol symbol) {
    if (symbol == null) {
      throw new IllegalArgumentException("symbol argument cannot be null");
    }
    _symbol = symbol;
  }

  public IRSymbol getSymbol() {
    return _symbol;
  }

  @Override
  public IRType getType() {
    return _symbol.getType();
  }
}
