/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import gw.plugin.ij.lang.GosuElementType;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;

public class GosuSpacingBeforeParentheses extends GosuElementTypes {
  private static boolean beforeLeftParen(PsiElement psi1, PsiElement psi2, GosuElementType type) {
    return psiElement(type).accepts(psi1) && psiElement(TT_OP_paren_left).accepts(psi2);
  }

  // Before parentheses
  private static Spacing getSpacingBeforeFunctionDeclarationParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_METHOD_PARENTHESES, settings);
  }

  private static Spacing getSpacingBeforeFunctionCallParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_METHOD_CALL_PARENTHESES, settings);
  }

  private static Spacing getSpacingBeforeIfParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_IF_PARENTHESES, settings);
  }

  private static Spacing getSpacingBeforeForParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_FOR_PARENTHESES, settings);
  }

  private static Spacing getSpacingBeforeWhileParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_WHILE_PARENTHESES, settings);
  }

  private static Spacing getSpacingBeforeSwitchParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_SWITCH_PARENTHESES, settings);
  }

  private static Spacing getSpacingBeforeCatchParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_CATCH_PARENTHESES, settings);
  }

  private static Spacing getSpacingBeforeAnnotationParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_ANOTATION_PARAMETER_LIST, settings);
  }

  private static Spacing getSpacingBeforeUsingParentheses(@NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    return GosuSpaces.getSpace(gosuSettings.SPACE_BEFORE_USING_PARENTHESES, settings);
  }

  private static Spacing getSpacingBeforeTypeOfParentheses(@NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    return GosuSpaces.getSpace(gosuSettings.SPACE_BEFORE_TYPEOF_PARENTHESES, settings);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    // function Identifier [SPACE] (Params)
    if (psiElement(TT_OP_paren_left)
        .withParent(psiElement(METHOD_DEFINITION))
        .accepts(psi2)) {
      return getSpacingBeforeFunctionDeclarationParentheses(settings);
    }

    // Identifier [SPACE] (Params)
    if (psiElement(TT_OP_paren_left)
        .withParent(or(psiElement(ELEM_TYPE_MethodCallExpression),
                       psiElement(ELEM_TYPE_BeanMethodCallExpression)))
        .accepts(psi2)) {
      return getSpacingBeforeFunctionCallParentheses(settings);
    }

    // if [SPACE] (
    if (beforeLeftParen(psi1, psi2, TT_if)) {
      return getSpacingBeforeIfParentheses(settings);
    }

    // for [SPACE] (
    if (beforeLeftParen(psi1, psi2, TT_for) || beforeLeftParen(psi1, psi2, TT_foreach)) {
      return getSpacingBeforeForParentheses(settings);
    }

    // while [SPACE] (
    if (beforeLeftParen(psi1, psi2, TT_while)) {
      return getSpacingBeforeWhileParentheses(settings);
    }

    // switch [SPACE] (
    if (beforeLeftParen(psi1, psi2, TT_switch)) {
      return getSpacingBeforeSwitchParentheses(settings);
    }

    // catch [SPACE] (
    if (beforeLeftParen(psi1, psi2, TT_catch)) {
      return getSpacingBeforeCatchParentheses(settings);
    }

    // using [SPACE] (
    if (beforeLeftParen(psi1, psi2, TT_using)) {
      return getSpacingBeforeUsingParentheses(settings, gosuSettings);
    }

    // typeof [SPACE] (
    if (psiElement(ELEM_TYPE_ParenthesizedExpression)
        .withParent(psiElement(ELEM_TYPE_TypeOfExpression))
        .accepts(psi2)) {
      return getSpacingBeforeTypeOfParentheses(settings, gosuSettings);
    }

    // @Annotation [SPACE] (
    if (psiElement(TT_OP_paren_left)
        .withParent(psiElement(ELEM_TYPE_AnnotationExpression))
        .accepts(psi2)) {
      return getSpacingBeforeAnnotationParentheses(settings);
    }

    return null;
  }
}
