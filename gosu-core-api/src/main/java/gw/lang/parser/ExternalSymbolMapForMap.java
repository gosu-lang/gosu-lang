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
    ISymbol symbol = _externalSymbols.get( name );
    if( symbol == null ) {
      symbol = getAltSymbol( name );
    }
    return symbol;
  }

  private ISymbol getAltSymbol( String name ) {
    String altName = handleCrappyPcfCapitalization( name );
    if( altName != null ) {
      return _externalSymbols.get( altName );
    }
    return null;
  }

  public boolean isExternalSymbol(String name) {
    if( !_externalSymbols.containsKey( name ) ) {
      return getAltSymbol( name ) != null;
    }
    return true;
  }

  public HashMap<String, ISymbol> getMap() {
    return _externalSymbols;
  }
}
