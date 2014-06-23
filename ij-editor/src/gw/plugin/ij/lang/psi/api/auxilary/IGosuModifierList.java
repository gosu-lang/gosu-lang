/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.auxilary;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierList;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.auxilary.annotation.IGosuAnnotation;
import org.jetbrains.annotations.NotNull;

public interface IGosuModifierList extends IGosuPsiElement, PsiModifierList {
  PsiElement[] getModifiers();

  boolean hasExplicitVisibilityModifiers();

  @NotNull
  IGosuAnnotation[] getAnnotations();
}
