/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.params;

import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiParameterList;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import org.jetbrains.annotations.NotNull;

public interface IGosuParameterList extends IGosuPsiElement, PsiParameterList, PsiModifierListOwner {
  @NotNull
  IGosuParameter[] getParameters();

  void addParameterToEnd(IGosuParameter parameter);

  void addParameterToHead(IGosuParameter parameter);

  int getParameterNumber(IGosuParameter parameter);
}
