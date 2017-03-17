/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IBlockClass;

public interface IBlockClassInternal extends IBlockClass, ICompilableTypeInternal
{
  public void update();
}
