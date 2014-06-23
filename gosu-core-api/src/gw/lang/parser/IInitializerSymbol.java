/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;

public interface IInitializerSymbol extends ITypedSymbol {
  IType getDeclaringTypeOfProperty();
}
