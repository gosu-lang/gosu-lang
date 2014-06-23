/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public enum SymbolType {
  CATCH_VARIABLE( true ),
  OBJECT_INITIALIZER( false ),
  NAMED_PARAMETER( false ),
  PARAMETER_DECLARATION( true ),
  FOREACH_VARIABLE( true );

  private boolean isLocal;

  private SymbolType( boolean isLocal ) {
    this.isLocal = isLocal;
  }

  public boolean isLocal() {
    return isLocal;
  }
}
