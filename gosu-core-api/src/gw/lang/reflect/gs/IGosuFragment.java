/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.IExpression;
import gw.lang.reflect.java.IJavaBackedTypeData;

public interface IGosuFragment extends ICompilableType, IJavaBackedTypeData {

  public static final String FRAGMENT_PACKAGE = "fragment_";

  Object evaluate(IExternalSymbolMap externalSymbols);

  Object evaluateRoot(IExternalSymbolMap externalSymbols);

  IExpression getExpression();

  void setExpression(IExpression expression);

  boolean isExternalSymbol(String name);
}
