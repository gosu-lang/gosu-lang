/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ILanguageLevel;

public class StandardLanguageLevel implements ILanguageLevel
{
  @Override
  public boolean allowAllImplicitCoercions()
  {
    return false;
  }

  @Override
  public boolean isStandard()
  {
    return true;
  }

  @Override
  public boolean supportsNakedCatchStatements()
  {
    return true;
  }
}
