/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements;

import com.intellij.psi.PsiStatement;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.IGosuPsiElement;

public interface IGosuStatement extends IGosuPsiElement, PsiStatement {
  IGosuStatement[] EMPTY_ARRAY = new IGosuStatement[0];

  <T extends IGosuStatement> T replaceWithStatement(T statement);

  void removeStatement() throws IncorrectOperationException;
}
