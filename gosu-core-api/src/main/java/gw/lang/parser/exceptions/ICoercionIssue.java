/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.reflect.IType;
import gw.lang.parser.IParseIssue;

public interface ICoercionIssue extends IParseIssue
{
  public IType getTypeToCoerceTo();

  public String getContextStringNoLineNumbers();
}
