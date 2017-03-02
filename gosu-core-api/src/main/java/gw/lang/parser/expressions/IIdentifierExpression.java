/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;

public interface IIdentifierExpression extends IExpression
{
  ISymbol getSymbol();

  void setSymbol( ISymbol symbol, ISymbolTable symTable );

  boolean isLocalVariable();
}
