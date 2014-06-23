/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IInitializerSymbol;
import gw.lang.parser.SymbolType;
import gw.lang.reflect.IType;

public class InitializerSymbol extends TypedSymbol implements IInitializerSymbol {
  private IType _declaringTypeOfProperty;

  public InitializerSymbol( String strProperty, IType declaringTypeOfProperty ) {
    super( strProperty, SymbolType.OBJECT_INITIALIZER );
    _declaringTypeOfProperty = declaringTypeOfProperty;
  }

  @Override
  public IType getDeclaringTypeOfProperty() {
    return _declaringTypeOfProperty;
  }
}
