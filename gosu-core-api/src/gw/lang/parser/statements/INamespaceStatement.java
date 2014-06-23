/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IStatement;

public interface INamespaceStatement extends IStatement
{
  String getNamespace();
}
