/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.arguments;

import com.intellij.psi.PsiNameValuePair;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import org.jetbrains.annotations.NotNull;

public interface IGosuNamedArgument extends IGosuPsiElement, PsiNameValuePair {
  IGosuNamedArgument[] EMPTY_ARRAY = new IGosuNamedArgument[0];

  @NotNull
  IGosuExpression getExpression();

  @NotNull
  String getLabelName();
}
