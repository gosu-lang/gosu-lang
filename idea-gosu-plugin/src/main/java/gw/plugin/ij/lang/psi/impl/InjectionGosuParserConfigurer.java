/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import gw.lang.parser.IGosuValidator;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.plugin.ij.util.InjectionUtil;

public class InjectionGosuParserConfigurer implements IGosuParserConfigurer {
  public static final Key<ISymbolTable> SYMBOL_TABLE = new Key<>("GOSU_SYMBOL_TABLE");
  public static final Key<IGosuValidator> VALIDATOR = new Key<>("GOSU_VALIDATOR");

  @Override
  public ISymbolTable getSymbolTable(AbstractGosuClassFileImpl psiFile) {
    PsiElement host = InjectionUtil.getInjectionHost(psiFile);

    if (host == null) {
      host = psiFile.getOriginalFile();
    }

    if (host != null) {
      final ISymbolTable table = host.getUserData(SYMBOL_TABLE);
      if (table != null) {
        return table;
      }
    }
    return null;
  }

  @Override
  public ITypeUsesMap getTypeUsesMap(AbstractGosuClassFileImpl psiFile) {
    return null;
  }

  @Override
  public IGosuValidator getValidator(AbstractGosuClassFileImpl psiFile) {
    PsiElement host = InjectionUtil.getInjectionHost(psiFile);

    if (host == null) {
      host = psiFile.getOriginalFile();
    }

    if (host != null) {
      final IGosuValidator validator = host.getUserData(VALIDATOR);
      if (validator != null) {
        return validator;
      }
    }
    return null;
  }
}
