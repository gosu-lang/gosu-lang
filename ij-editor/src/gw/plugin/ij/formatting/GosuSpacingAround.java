/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import gw.plugin.ij.lang.GosuTokenSets;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class GosuSpacingAround {
  // Around
  private static Spacing getSpacingAroundAssignmentOperators(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AROUND_ASSIGNMENT_OPERATORS, settings);
  }

  private static Spacing getSpacingAroundLogicalOperators(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AROUND_LOGICAL_OPERATORS, settings);
  }

  private static Spacing getSpacingAroundEqualityOperators(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AROUND_EQUALITY_OPERATORS, settings);
  }

  private static Spacing getSpacingAroundRelationalOperators(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AROUND_RELATIONAL_OPERATORS, settings);
  }

  private static Spacing getSpacingAroundBitwiseOperators(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AROUND_BITWISE_OPERATORS, settings);
  }

  private static Spacing getSpacingAroundAdditiveOperators(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AROUND_ADDITIVE_OPERATORS, settings);
  }

  private static Spacing getSpacingAroundMultiplicativeOperators(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS, settings);
  }

  private static Spacing getSpacingAroundShiftOperators(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AROUND_SHIFT_OPERATORS, settings);
  }

  private static Spacing getSpacingAroundUnaryOperators(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AROUND_UNARY_OPERATOR, settings);
  }

  private static Spacing getSpacingAroundIntervalOperators(@NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    return GosuSpaces.getSpace(gosuSettings.SPACE_AROUND_INTERVAL_OPERATORS, settings);
  }

  private static boolean checkBoth(PsiElement psi1, PsiElement psi2, TokenSet set) {
    final PsiElementPattern element = psiElement().withElementType(set);
    return element.accepts(psi1) || element.accepts(psi2);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    if (checkBoth(psi1, psi2, GosuTokenSets.ASSIGNMENT_OPS)) {
      return getSpacingAroundAssignmentOperators(settings);
    }

    if (checkBoth(psi1, psi2, GosuTokenSets.LOGICAL_OPS)) {
      return getSpacingAroundLogicalOperators(settings);
    }

    if (checkBoth(psi1, psi2, GosuTokenSets.EQUALITY_OPS)) {
      return getSpacingAroundEqualityOperators(settings);
    }

    if (checkBoth(psi1, psi2, GosuTokenSets.RELATIONAL_OPS) &&
        !(psi1 instanceof GosuTypeLiteralImpl) &&
        !(psi2 instanceof GosuTypeLiteralImpl)) {
      return getSpacingAroundRelationalOperators(settings);
    }

    if (checkBoth(psi1, psi2, GosuTokenSets.BITWISE_OPS)) {
      return getSpacingAroundBitwiseOperators(settings);
    }

    if (checkBoth(psi1, psi2, GosuTokenSets.ADDITIVE_OPS)) {
      return getSpacingAroundAdditiveOperators(settings);
    }

    if (checkBoth(psi1, psi2, GosuTokenSets.MULTIPLICATIVE_OPS)) {
      return getSpacingAroundMultiplicativeOperators(settings);
    }

    if (checkBoth(psi1, psi2, GosuTokenSets.SHIFT_OPS)) {
      return getSpacingAroundShiftOperators(settings);
    }

    if (checkBoth(psi1, psi2, GosuTokenSets.UNARY_OPS)) {
      return getSpacingAroundUnaryOperators(settings);
    }

    if (checkBoth(psi1, psi2, GosuTokenSets.INTERVAL_OPS)) {
      return getSpacingAroundIntervalOperators(settings, gosuSettings);
    }

    return null;
  }
}
