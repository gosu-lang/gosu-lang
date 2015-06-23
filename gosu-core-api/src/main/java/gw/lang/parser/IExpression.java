/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IExternalSymbolMap;

public interface IExpression extends IParsedElement, IHasType
{
  public static final IExpression[] EMPTY_ARRAY = new IExpression[0];

  /**
   * Evaluates this Expression and returns the result.
   */
  Object evaluate();

  Object evaluate(IExternalSymbolMap externalSymbols);

  IType getContextType();

  boolean isNullSafe();

  boolean isUnchecked();
}
