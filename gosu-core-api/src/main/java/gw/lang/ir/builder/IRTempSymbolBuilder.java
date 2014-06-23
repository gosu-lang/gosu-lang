/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.ir.IRType;
import gw.lang.ir.IRSymbol;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRTempSymbolBuilder extends IRSymbolBuilder {

  private IRType _type;
  private IRSymbol _symbol;

  public IRTempSymbolBuilder(IRType type) {
    _type = type;
  }

  @Override
  public IRSymbol build(IRBuilderContext context) {
    if (_symbol == null) {
      _symbol = context.tempSymbol(_type);
    }
    return _symbol;
  }
}
