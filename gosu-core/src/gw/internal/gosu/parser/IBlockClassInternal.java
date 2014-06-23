/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IBlockClass;
import gw.internal.gosu.parser.ICompilableTypeInternal;

public interface IBlockClassInternal extends IBlockClass, ICompilableTypeInternal
{
  public void update();
}
