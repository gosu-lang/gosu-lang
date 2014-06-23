/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.arguments;

import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.api.util.GosuNamedArgumentsOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IGosuArgumentList extends IGosuPsiElement, GosuNamedArgumentsOwner {
  @NotNull
  IGosuExpression[] getExpressionArguments();

  @NotNull
  PsiElement[] getAllArguments();

  @NotNull
  IGosuArgumentList replaceWithArgumentList(IGosuArgumentList newArgList) throws IncorrectOperationException;

  boolean isIndexPropertiesList();

  @Nullable
  PsiElement getLeftParen();

  @Nullable
  PsiElement getRightParen();

  int getExpressionArgumentIndex(IGosuExpression arg);

  @Nullable
  IGosuExpression removeArgument(int argNumber);

  @NotNull
  IGosuNamedArgument addNamedArgument(IGosuNamedArgument namedArgument);
}