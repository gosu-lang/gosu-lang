/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.reflect.IType;

public abstract class FragmentInstance implements IGosuObject {

  public abstract Object evaluate(IExternalSymbolMap symbols);

  @SuppressWarnings({"UnusedDeclaration"})
  public Object evaluateRootExpression(IExternalSymbolMap symbols) {
    return null;
  }

  @Override
  public IType getIntrinsicType() {
    return null;
  }
}
