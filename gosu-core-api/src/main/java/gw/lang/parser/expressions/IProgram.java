/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.parser.IStatement;

import java.util.Map;

public interface IProgram extends IExpression
{
  IStatement getMainStatement();

  Map getFunctions();

  boolean hasContent();

}
