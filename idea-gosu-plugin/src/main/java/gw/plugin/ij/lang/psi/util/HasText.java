/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiMatcherExpression;
import gw.plugin.ij.lang.psi.IGosuPsiElement;

import java.util.Objects;

public class HasText implements PsiMatcherExpression {

  private final String text;

  public HasText(String text) {
    this.text = text;
  }

  public Boolean match(PsiElement element) {
    return text.equals(element.getText());
  }
}