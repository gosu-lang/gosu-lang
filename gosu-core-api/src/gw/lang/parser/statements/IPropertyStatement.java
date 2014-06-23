/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IStatement;

public interface IPropertyStatement extends IStatement, IParsedElementWithAtLeastOneDeclaration
{
  IFunctionStatement getPropertyGetterOrSetter();

  IDynamicPropertySymbol getDps();
}
