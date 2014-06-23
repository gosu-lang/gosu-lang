/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.JavaFindUsagesHandler;
import com.intellij.find.findUsages.JavaFindUsagesHandlerFactory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.refactoring.util.JavaNonCodeSearchElementDescriptionProvider;
import com.intellij.refactoring.util.NonCodeSearchDescriptionLocation;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;

public class GosuFindUsagesHandler extends JavaFindUsagesHandler {

  public GosuFindUsagesHandler(@org.jetbrains.annotations.NotNull PsiElement psiElement, JavaFindUsagesHandlerFactory factory) {
    super(psiElement, factory);
  }

  public GosuFindUsagesHandler(@org.jetbrains.annotations.NotNull PsiElement psiElement, @org.jetbrains.annotations.NotNull PsiElement[] elementsToSearch, JavaFindUsagesHandlerFactory factory) {
    super(psiElement, elementsToSearch, factory);
  }

  @Override
  protected boolean isSearchForTextOccurencesAvailable(PsiElement psiElement, boolean isSingleFile) {
    if (psiElement instanceof IGosuTypeDefinition) {
      return false;
    }
    return super.isSearchForTextOccurencesAvailable(psiElement, isSingleFile);
  }
}
