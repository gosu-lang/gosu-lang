/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.gs.IGosuProgram;

public interface IParseResult extends IHasType
{
  IExpression getExpression();
  IStatement getStatement();

  IExpression getRawExpression();
  IGosuProgram getProgram();

  IParsedElement getParsedElement();

  Object evaluate();

  boolean isLiteral();
  boolean isProgram();
}