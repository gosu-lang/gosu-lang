/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;

public interface ITypeVariableDefinitionExpression extends IParsedElementWithAtLeastOneDeclaration
{
  public ITypeVariableDefinition getTypeVarDef();  
}
