/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import gw.lang.parser.IGosuValidator;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;

public interface IGosuParserConfigurer {
  ISymbolTable getSymbolTable(AbstractGosuClassFileImpl psiFile);

  ITypeUsesMap getTypeUsesMap(AbstractGosuClassFileImpl psiFile);

  IGosuValidator getValidator(AbstractGosuClassFileImpl psiFile);
}
