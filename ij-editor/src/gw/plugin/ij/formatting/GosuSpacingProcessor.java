/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.GosuTokenSets;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;

public class GosuSpacingProcessor {
  @Nullable
  public static Spacing getSpacing(@NotNull GosuBlock child1, @NotNull GosuBlock child2, @NotNull CodeStyleSettings settings) {
    final PsiElement psi1 = child1.getNode().getPsi();
    final PsiElement psi2 = child2.getNode().getPsi();

    final CommonCodeStyleSettings commonSettings = settings.getCommonSettings(GosuLanguage.instance());
    final GosuCodeStyleSettings gosuSettings= settings.getCustomSettings(GosuCodeStyleSettings.class);

    Spacing spacing;
    if ((spacing = GosuSpacingNonConfigured.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = GosuSpacingWithinTypeArguments.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = GosuSpacingBeforeParentheses.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = GosuSpacingAround.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = GosuSpacingBeforeLeftBrace.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = GosuSpacingBeforeKeywords.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = GosuSpacingWithin.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = GosuSpacingInTernaryOperator.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = GosuSpacingInBlocks.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = GosuSpacingOther.getSpacing(psi1, psi2, commonSettings, gosuSettings)) != null) {
      return spacing;
    }

    return null;
  }
}
