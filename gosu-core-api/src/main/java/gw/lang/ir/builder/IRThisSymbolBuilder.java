/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.ir.IRSymbol;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRThisSymbolBuilder extends IRSymbolBuilder {

  @Override
  public IRSymbol build(IRBuilderContext context) {
    return new IRSymbol("this", context.owningType(), false);
  }
}
