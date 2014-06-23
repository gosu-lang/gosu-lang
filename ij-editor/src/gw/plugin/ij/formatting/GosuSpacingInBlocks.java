/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class GosuSpacingInBlocks extends GosuElementTypes {
  private static Spacing getSpacingAfterLambda(@NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    return GosuSpaces.getSpace(gosuSettings.SPACE_AFTER_LAMBDA, settings);
  }

  private static Spacing getSpacingAroundArrow(@NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    return GosuSpaces.getSpace(gosuSettings.SPACE_AROUND_ARROW, settings);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    // \ [SPACE] x -> x + 1
    if (psiElement(TT_OP_escape)
        .withParent(psiElement(ELEM_TYPE_BlockExpression))
        .accepts(psi1)) {
      return getSpacingAfterLambda(settings, gosuSettings);
    }

    // \ x [SPACE] -> [SPACE] x + 1
    final ElementPattern<PsiElement> pattern = psiElement(TT_OP_assign_closure).withParent(psiElement(ELEM_TYPE_BlockExpression));
    if (pattern.accepts(psi2) || pattern.accepts(psi1)) {
      return getSpacingAroundArrow(settings, gosuSettings);
    }

    return null;
  }
}
