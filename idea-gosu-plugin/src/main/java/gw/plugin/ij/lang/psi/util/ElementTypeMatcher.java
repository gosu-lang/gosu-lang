/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiMatcherExpression;
import gw.plugin.ij.lang.GosuElementType;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import org.jetbrains.annotations.NotNull;

public class ElementTypeMatcher implements PsiMatcherExpression {
  private final GosuElementType elementType;

  public ElementTypeMatcher(GosuElementType elementType) {
    this.elementType = elementType;
  }

  @NotNull
  @Override
  public Boolean match(@NotNull PsiElement element) {
    if (element instanceof GosuBaseElementImpl) {
      return elementType == ((GosuBaseElementImpl) element).getGosuElementType();
    }
    return false;
  }
}
