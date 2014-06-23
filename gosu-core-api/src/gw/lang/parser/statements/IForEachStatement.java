/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IStatement;
import gw.lang.parser.ISymbol;

public interface IForEachStatement extends ILoopStatement, IParsedElementWithAtLeastOneDeclaration
{
  ISymbol getIdentifier();

  ISymbol getIndexIdentifier();

  IExpression getInExpression();

  IStatement getStatement();
}
