/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import java.util.List;

import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IParameterDeclaration;

public interface IFunctionStatement extends IStatement, IParsedElementWithAtLeastOneDeclaration
{
  IDynamicFunctionSymbol getDynamicFunctionSymbol();
  
  List<IParameterDeclaration> getParameters();
}
