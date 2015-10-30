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
    return _table.getSymbol( name );
  }
}
