/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface IHasArguments
{
  public int getArgPosition();

  public IExpression[] getArgs();
}
