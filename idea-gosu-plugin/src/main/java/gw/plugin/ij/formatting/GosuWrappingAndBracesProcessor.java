/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class GosuWrappingAndBracesProcessor {
  public static Spacing getSpacing(GosuBlock child1, GosuBlock child2, CodeStyleSettings settings) {
//    final PsiElement psi1 = child1.getNode().getPsi();
//    final PsiElement psi2 = child2.getNode().getPsi();
//
//    final CommonCodeStyleSettings commonSettings = settings.getCommonSettings(GosuLanguage.instance());
//    //final GosuCodeStyleSettings gosuSettings = settings.getCustomSettings(GosuCodeStyleSettings.class);
//
//    if (!commonSettings.KEEP_MULTIPLE_EXPRESSIONS_IN_ONE_LINE &&
//        (psi1 instanceof GosuVariableImpl) && !(psi1 instanceof GosuParameterImpl)) {
//      return GosuSpaces.blankLines(0);
//    }
    return null;
  }
}
