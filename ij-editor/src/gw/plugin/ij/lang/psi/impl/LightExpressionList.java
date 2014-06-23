/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.light.LightElement;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.impl.expressions.GosuExpressionListImpl;
import org.jetbrains.annotations.NotNull;

public class LightExpressionList extends LightElement implements PsiExpressionList {
  private final GosuExpressionListImpl gosuArgumentList;

  public LightExpressionList(PsiManager manager, GosuExpressionListImpl gosuArgumentList) {
    super(manager, GosuLanguage.instance());
    this.gosuArgumentList = gosuArgumentList;
  }

  @NotNull
  @Override
  public PsiExpression[] getExpressions() {
    return gosuArgumentList != null ? gosuArgumentList.getExpressions() : new PsiExpression[0];
  }

  @NotNull
  @Override
  public PsiType[] getExpressionTypes() {
    return gosuArgumentList != null ? gosuArgumentList.getExpressionTypes() : new PsiType[0];
  }

  @NotNull
  @Override
  public String toString() {
    return "";
  }
}
