/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.ISourceFileHandle;

import java.util.Collections;

public class StandardLanguageLevel implements ILanguageLevel
{
  private static final ITypeUsesMap EMPTY_TYPE_USES = new TypeUsesMap( Collections.<String>emptyList() ).lock();

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

  @Override
  public boolean isUnreachableCodeDetectionOn() {
    return true;
  }

  @Override
  public boolean isWarnOnImplicitCoercionsOn() {
    return true;
  }

  @Override
  public boolean areUsesStatementsAllowedInStatementLists(ICompilableType gosuClass) {
    return false;
  }

  @Override
  public boolean shouldAddWarning(IType type, IParseIssue warning) {
    return true;
  }

  @Override
  public ITypeUsesMap getDefaultTypeUses() {
    return EMPTY_TYPE_USES;
  }

}
