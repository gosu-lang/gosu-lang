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

public class GosuSpacingOther extends GosuElementTypes {
  private static Spacing getSpacingBeforeComma(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_COMMA, settings);
  }

  private static Spacing getSpacingAfterComma(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AFTER_COMMA, settings);
  }

  private static Spacing getSpacingBeforeColon(@NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    return GosuSpaces.getSpace(gosuSettings.SPACE_BEFORE_COLON, settings);
  }

  private static Spacing getSpacingAfterColon(@NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    return GosuSpaces.getSpace(gosuSettings.SPACE_AFTER_COLON, settings);
  }

  private static Spacing getSpacingAfterTypeCast(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AFTER_TYPE_CAST, settings);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    // smth [SPACE] ,
    if (psiElement(TT_OP_comma).accepts(psi2)) {
      return getSpacingBeforeComma(settings);
    }

    // , [SPACE] smth
    if (psiElement(TT_OP_comma).accepts(psi1)) {
      return getSpacingAfterComma(settings);
    }

    // smth [SPACE] :
    // Must be after GosuSpacingInTernaryOperator
    if (psiElement(TT_OP_colon).accepts(psi2)) {
      return getSpacingBeforeColon(settings, gosuSettings);
    }

    // : [SPACE] smth
    // Must be after GosuSpacingInTernaryOperator
    if (psiElement(TT_OP_colon).accepts(psi1)) {
      return getSpacingAfterColon(settings, gosuSettings);
    }

    // (Type) [SPACE] smth
    if (psiElement(ELEM_TYPE_ParenthesizedExpression)
        .withParent(psiElement(ELEM_TYPE_TypeAsExpression))
        .accepts(psi1)) {
      return getSpacingAfterTypeCast(settings);
    }

    return null;
  }
}
