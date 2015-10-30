/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import java.util.HashMap;

public class ExternalSymbolMapForMap extends ExternalSymbolMapBase {

  private HashMap<String, ISymbol> _externalSymbols;

  public ExternalSymbolMapForMap( HashMap<String, ISymbol> externalSymbols) {
    this(externalSymbols, false);
  }

  public ExternalSymbolMapForMap( HashMap<String, ISymbol> externalSymbols, boolean assumeSymbolsRequireExternalSymbolMapArgument) {
    super(assumeSymbolsRequireExternalSymbolMapArgument);
    _externalSymbols = externalSymbols;
  }

  public ISymbol getSymbol(String name) {
    return _externalSymbols.get( name );
  }

  public boolean isExternalSymbol(String name) {
    return _externalSymbols.containsKey( name );
  }

  public HashMap<String, ISymbol> getMap() {
    return _externalSymbols;
  }
}
