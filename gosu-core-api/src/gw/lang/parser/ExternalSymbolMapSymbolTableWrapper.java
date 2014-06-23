/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public class ExternalSymbolMapSymbolTableWrapper extends ExternalSymbolMapBase {
  private ISymbolTable _table;

  public ExternalSymbolMapSymbolTableWrapper(ISymbolTable table) {
    this(table, false);
  }

  public ExternalSymbolMapSymbolTableWrapper(ISymbolTable table, boolean assumeSymbolsRequireExternalSymbolMapArgument) {
    super(assumeSymbolsRequireExternalSymbolMapArgument);
    _table = table;
  }

  public ISymbol getSymbol( String name ) {
    ISymbol symbol = _table.getSymbol( name );
    if( symbol == null ) {
      symbol = getAltSymbol( name );
    }
    return symbol;
  }

  private ISymbol getAltSymbol( String name ) {
    String altName = handleCrappyPcfCapitalization( name );
    if( altName != null ) {
      return _table.getSymbol( altName );
    }
    return null;
  }
}
