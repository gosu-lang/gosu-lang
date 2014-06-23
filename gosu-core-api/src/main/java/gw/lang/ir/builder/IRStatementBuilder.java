/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRStatement;

@UnstableAPI
public abstract class IRStatementBuilder extends IRElementBuilder {
  private IRStatement _statement;

  public IRStatement build(IRBuilderContext context) {
    if (_statement == null) {
      _statement = buildImpl(context);
    }
    return _statement;
  }

  protected abstract IRStatement buildImpl(IRBuilderContext context);

}
