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
import static com.intellij.patterns.StandardPatterns.or;

public class GosuSpacingWithinTypeArguments extends GosuElementTypes {
  private static Spacing getSpacingWithinTypeArgumentsAfterComma(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_AFTER_COMMA_IN_TYPE_ARGUMENTS, settings);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    // class Identifier<A, [SPACE] B>
    if (psiElement(TT_OP_comma)
        .withParent(psiElement(ELEM_TYPE_TypeVariableListClause))
        .accepts(psi1)) {
      return getSpacingWithinTypeArgumentsAfterComma(settings);
    }

    // class Identifier [SPACE] <A>
    // TODO: new Class [SPACE] <Object>[0]
    if (psiElement(ELEM_TYPE_TypeVariableListClause)
        .accepts(psi2)) {
      return GosuSpaces.EMPTY;
    }

    // class Identifier< [SPACE] A>
    // new Class< [SPACE] Object>[0]
    if (psiElement(TT_OP_less)
        .withParent(or(psiElement(ELEM_TYPE_TypeVariableListClause),
                       psiElement(ELEM_TYPE_TypeLiteral)))
        .accepts(psi1)) {
      return GosuSpaces.EMPTY;
    }

    // class Identifier<A [SPACE] >
    // new Class <Object [SPACE] >[0]
    if (psiElement(TT_OP_greater)
        .withParent(or(psiElement(ELEM_TYPE_TypeVariableListClause),
                       psiElement(ELEM_TYPE_TypeLiteral)))
        .accepts(psi2)) {
      return GosuSpaces.EMPTY;
    }

    return null;
  }
}
