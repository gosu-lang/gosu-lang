/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.util;

import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatement;
import org.jetbrains.annotations.NotNull;

public interface GosuStatementOwner extends IGosuPsiElement {
  @NotNull
  IGosuStatement addStatementBefore(IGosuStatement statement, IGosuStatement anchor) throws IncorrectOperationException;

  void removeElements(PsiElement[] elements) throws IncorrectOperationException;

}
