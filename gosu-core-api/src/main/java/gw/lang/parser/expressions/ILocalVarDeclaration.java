/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.ISymbol;

public interface ILocalVarDeclaration extends IExpression, IParsedElementWithAtLeastOneDeclaration
{
  CharSequence getLocalVarName();

  ITypeLiteralExpression getTypeLiteral();

  ISymbol getSymbol();
}
