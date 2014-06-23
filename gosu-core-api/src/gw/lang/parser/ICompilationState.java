/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface ICompilationState
{
  boolean isCompilingHeader();

  boolean isHeaderCompiled();

  boolean isCompilingDeclarations();

  boolean isDeclarationsCompiled();

  boolean isCompilingDefinitions();

  boolean isDefinitionsCompiled();
}
