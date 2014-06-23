/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class GosuSpacingBeforeKeywords extends GosuElementTypes {
  private static Spacing getSpacingBeforeKeywordElse(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_ELSE_KEYWORD, settings);
  }

  private static Spacing getSpacingBeforeKeywordWhile(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_WHILE_KEYWORD, settings);
  }

  private static Spacing getSpacingBeforeKeywordCatch(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_CATCH_KEYWORD, settings);
  }

  private static Spacing getSpacingBeforeKeywordFinally(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_FINALLY_KEYWORD, settings);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    if (psiElement(TT_else).accepts(psi2)) {
      return getSpacingBeforeKeywordElse(settings);
    }

    if (psiElement(TT_while).accepts(psi2)) {
      return getSpacingBeforeKeywordWhile(settings);
    }

    if (psiElement(ELEM_TYPE_CatchClause).accepts(psi2)) {
      return getSpacingBeforeKeywordCatch(settings);
    }

    if (psiElement(TT_finally).accepts(psi2)) {
      return getSpacingBeforeKeywordFinally(settings);
    }

    return null;
  }
}
