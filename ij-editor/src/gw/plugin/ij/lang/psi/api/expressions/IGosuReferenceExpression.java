/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.expressions;

import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiQualifiedReference;
import com.intellij.psi.PsiType;
import gw.lang.parser.IParsedElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import org.jetbrains.annotations.Nullable;

public interface IGosuReferenceExpression<T extends IParsedElement> extends IGosuExpression, IGosuPsiElement, PsiPolyVariantReference, PsiQualifiedReference {
  PsiType[] getTypeArguments();

  @Nullable
  IGosuTypeParameterList getTypeParameterList();
}
