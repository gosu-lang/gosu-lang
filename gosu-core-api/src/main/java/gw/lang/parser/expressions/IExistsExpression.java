/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.ISymbol;

public interface IExistsExpression extends IExpression, IParsedElementWithAtLeastOneDeclaration
{
  ISymbol getIdentifier();

  ISymbol getIndexIdentifier();

  IExpression getInExpression();

  IExpression getWhereExpression();
}
