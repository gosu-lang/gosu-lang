/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.expressions;

import com.intellij.psi.PsiExpression;
import gw.plugin.ij.lang.psi.IGosuPsiElement;

public interface IGosuExpression extends IGosuPsiElement, PsiExpression {
  IGosuExpression[] EMPTY_ARRAY = new IGosuExpression[0];
}
