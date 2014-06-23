/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.gs.ITemplateType;

/**
 */
public interface IGosuTemplateInternal extends ITemplateType, IGosuProgramInternal
{
  void addTemplateEntryPoints( ISymbolTable symbolTable, GosuClassParser gosuClassParser );
}