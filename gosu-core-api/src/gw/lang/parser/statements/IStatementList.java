/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IStatement;

public interface IStatementList extends IStatement
{
  IStatement[] getStatements();

  boolean hasScope();
}
