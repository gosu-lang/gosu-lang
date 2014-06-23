/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IStatement;

import java.util.List;

public interface ITryCatchFinallyStatement extends IStatement
{
  IStatement getTryStatement();

  List<? extends ICatchClause> getCatchStatements();

  IStatement getFinallyStatement();
}
