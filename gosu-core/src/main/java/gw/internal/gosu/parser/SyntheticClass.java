/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.IType;
import gw.lang.parser.ITypeUsesMap;
import gw.internal.gosu.parser.statements.ClassStatement;

/**
 * A base class for creating synthetic Gosu classes that do not correspond directly to
 * a Gosu class
 */
public class SyntheticClass extends GosuClass {

  public SyntheticClass(String strNamespace, String strRelativeName, GosuClassTypeLoader classTypeLoader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap) {
    super(strNamespace, strRelativeName, classTypeLoader, sourceFile, typeUsesMap);
  }

  protected void initCompilationState()
  {
    // No compilation is necessary for this class
    getCompilationState().setHeaderCompiled();
    getCompilationState().setDefinitionsCompiled();
    getCompilationState().setDeclarationsCompiled();
  }
}
