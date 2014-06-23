/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import com.intellij.codeInsight.hint.DeclarationRangeHandler;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.impl.statements.GosuVariableImpl;
import org.jetbrains.annotations.NotNull;

public class GosuVariableRangeHandler implements DeclarationRangeHandler {
  @NotNull
  @Override
  public TextRange getDeclarationRange(@NotNull PsiElement container) {
    if (container instanceof GosuVariableImpl) {
      final GosuVariableImpl varImpl = (GosuVariableImpl) container;
      final IGosuExpression initializer = varImpl.getInitializerGosu();
      int startOffset = varImpl.getTextRange().getStartOffset();
      int endOffset = initializer == null ? varImpl.getTextRange().getEndOffset() : initializer.getTextRange().getStartOffset() - 1;
      return new TextRange(startOffset, endOffset);
    }
    return TextRange.EMPTY_RANGE;
  }
}
