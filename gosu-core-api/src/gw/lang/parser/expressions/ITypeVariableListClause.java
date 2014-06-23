/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IParsedElement;

import java.util.List;

public interface ITypeVariableListClause extends IParsedElement
{
  List<ITypeVariableDefinitionExpression> getTypeVariabledDefinitions();
}
