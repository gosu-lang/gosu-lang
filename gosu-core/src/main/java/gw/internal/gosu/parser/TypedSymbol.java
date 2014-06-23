/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypedSymbol;
import gw.lang.parser.SymbolType;
import gw.lang.reflect.IType;

public class TypedSymbol extends Symbol implements ITypedSymbol {
  private SymbolType _symbolType;

  public TypedSymbol( String strValue, SymbolType symbolType ) {
    super( strValue, null, null );
    _symbolType = symbolType;
  }

  public TypedSymbol( String strIdentifier, IType type, ISymbolTable symTable, Object value, SymbolType symbolType ) {
    super( strIdentifier, type, symTable, value );
    _symbolType = symbolType;
  }

  @Override
  public boolean isImplicitlyInitialized() {
    return isLocal() && isFinal();
  }

  public ISymbol getLightWeightReference() {
    return this;
  }

  @Override
  public boolean isLocal() {
    return _symbolType.isLocal();
  }

  @Override
  public SymbolType getSymbolType() {
    return _symbolType;
  }
}
