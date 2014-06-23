/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiMatcherExpression;
import gw.plugin.ij.lang.psi.IGosuPsiElement;

public class HasParsedElement implements PsiMatcherExpression {

  private final Class<?> parsedElementType;

  public HasParsedElement(Class<?> parsedElementType) {
    this.parsedElementType = parsedElementType;
  }

  public Boolean match(PsiElement element) {
    if (element instanceof IGosuPsiElement) {
      return parsedElementType.isInstance(((IGosuPsiElement) element).getParsedElement());
    }
    return false;
  }
}