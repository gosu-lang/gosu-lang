/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class GosuSpacingInTernaryOperator extends GosuElementTypes {
  // Ternary
  private static Spacing getSpacingBeforeQuestionInTernaryOperator(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_QUEST, settings);
  }

  private static Spacing getSpacingAfterQuestionInTernaryOperator(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AFTER_QUEST, settings);
  }

  private static Spacing getSpacingBeforeColonInTernaryOperator(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_COLON, settings);
  }

  private static Spacing getSpacingAfterColonInTernaryOperator(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AFTER_COLON, settings);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    // Condition [SPACE] ? TrueExpr : FalseExpr
    if (psiElement(TT_OP_question)
        .withParent(psiElement(ELEM_TYPE_ConditionalTernaryExpression))
        .accepts(psi2)) {
      return getSpacingBeforeQuestionInTernaryOperator(settings);
    }

    // Condition ? [SPACE] TrueExpr : FalseExpr
    if (psiElement(TT_OP_question)
        .withParent(psiElement(ELEM_TYPE_ConditionalTernaryExpression))
        .accepts(psi1)) {
      return getSpacingAfterQuestionInTernaryOperator(settings);
    }

    // Condition ? TrueExpr [SPACE] : FalseExpr
    // Must be before GosuSpacingOther
    if (psiElement(TT_OP_colon)
        .withParent(psiElement(ELEM_TYPE_ConditionalTernaryExpression))
        .accepts(psi2)) {
      return getSpacingBeforeColonInTernaryOperator(settings);
    }

    // Condition ? TrueExpr : [SPACE] FalseExpr
    // Must be before GosuSpacingOther
    if (psiElement(TT_OP_colon)
        .withParent(psiElement(ELEM_TYPE_ConditionalTernaryExpression))
        .accepts(psi1)) {
      return getSpacingAfterColonInTernaryOperator(settings);
    }

    return null;
  }
}
