/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import gw.plugin.ij.lang.GosuElementType;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class GosuSpacingWithin extends GosuElementTypes {
  private static boolean inParentheses(PsiElement psi1, PsiElement psi2, GosuElementType type) {
    // ( [SPACE] type
    if (psiElement(TT_OP_paren_left).accepts(psi1) &&
        psiElement(type).accepts(psi2)) {
      return true;
    }

    // type [SPACE] )
    if (psiElement(type).accepts(psi1) &&
        psiElement(TT_OP_paren_right).accepts(psi2)) {
      return true;
    }

    return false;
  }

  private static boolean inParenthesesWithParent(PsiElement psi1, PsiElement psi2, @NotNull ElementPattern<PsiElement> parent) {
    return psiElement(TT_OP_paren_left).withParent(parent).accepts(psi1) ||
           psiElement(TT_OP_paren_right).withParent(parent).accepts(psi2);
  }

  // Within
  private static Spacing getSpacingWithinBrackets(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_BRACKETS, settings);
  }

  private static Spacing getSpacingWithinFunctionDeclarationParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_METHOD_PARENTHESES, settings);
  }

  private static Spacing getSpacingWithinFunctionCallParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_METHOD_CALL_PARENTHESES, settings);
  }

  private static Spacing getSpacingWithinIfParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_IF_PARENTHESES, settings);
  }

  private static Spacing getSpacingWithinForParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_FOR_PARENTHESES, settings);
  }

  private static Spacing getSpacingWithinWhileParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_WHILE_PARENTHESES, settings);
  }

  private static Spacing getSpacingWithinSwitchParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_SWITCH_PARENTHESES, settings);
  }

  private static Spacing getSpacingWithinCatchParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_CATCH_PARENTHESES, settings);
  }

  private static Spacing getSpacingWithinCastParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_CAST_PARENTHESES, settings);
  }

  private static Spacing getSpacingWithinAnnotationParentheses(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_WITHIN_ANNOTATION_PARENTHESES, settings);
  }

  private static Spacing getSpacingWithingTypeOfParentheses(@NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    return GosuSpaces.getSpace(gosuSettings.SPACE_WITHIN_TYPEOF_PARENTHESES, settings);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    // @Annotation ( [SPACE] smth [SPACE] )
    if (inParenthesesWithParent(psi1, psi2, psiElement(ELEM_TYPE_AnnotationExpression))) {
      return getSpacingWithinAnnotationParentheses(settings);
    }

    // if ([SPACE] * [SPACE]) {
    if (inParenthesesWithParent(psi1, psi2, psiElement(ELEM_TYPE_IfStatement))) {
      return getSpacingWithinIfParentheses(settings);
    }

    // for ([SPACE] * [SPACE]) {
    if (inParenthesesWithParent(psi1, psi2, psiElement(ELEM_TYPE_ForEachStatement))) {
      return getSpacingWithinForParentheses(settings);
    }

    // while ([SPACE] * [SPACE]) {
    if (inParenthesesWithParent(psi1, psi2, psiElement(ELEM_TYPE_WhileStatement))) {
      return getSpacingWithinWhileParentheses(settings);
    }

    // switch ([SPACE] * [SPACE]) {
    if (inParenthesesWithParent(psi1, psi2, psiElement(ELEM_TYPE_SwitchStatement))) {
      return getSpacingWithinSwitchParentheses(settings);
    }

    // catch ([SPACE] * [SPACE]) {
    if (inParenthesesWithParent(psi1, psi2, psiElement(ELEM_TYPE_CatchClause))) {
      return getSpacingWithinCatchParentheses(settings);
    }

    // int[] array = ([SPACE] int[] [SPACE])obj
    if (inParenthesesWithParent(psi1, psi2,
         psiElement(ELEM_TYPE_ParenthesizedExpression)
         .withParent(psiElement(ELEM_TYPE_TypeAsExpression)))) {
      return getSpacingWithinCastParentheses(settings);
    }

    // typeof( [SPACE] expr [SPACE] )
    if (inParenthesesWithParent(psi1, psi2, psiElement(ELEM_TYPE_ParenthesizedExpression).
        withParent(psiElement(ELEM_TYPE_TypeOfExpression)))) {
      return getSpacingWithingTypeOfParentheses(settings, gosuSettings);
    }

    // ( [SPACE] ParameterList [SPACE] )
    if (inParentheses(psi1, psi2, ELEM_TYPE_ParameterListClause)) {
      return getSpacingWithinFunctionDeclarationParentheses(settings);
    }

    // ( [SPACE] ArgumentList [SPACE] )
    if (inParentheses(psi1, psi2, ELEM_TYPE_ArgumentListClause)) {
      return getSpacingWithinFunctionCallParentheses(settings);
    }

    // ( [SPACE] )
    if (psiElement(TT_OP_paren_left).accepts(psi1) &&
        psiElement(TT_OP_paren_right).accepts(psi2)) {
      return GosuSpaces.EMPTY;
    }

    // [ [SPACE] smth [SPACE] ]
    if (psiElement(TT_OP_bracket_left)
            .withParent(psiElement(ELEM_TYPE_ArrayAccess))
            .accepts(psi1) ||
        psiElement(TT_OP_bracket_right)
            .withParent(psiElement(ELEM_TYPE_ArrayAccess))
            .accepts(psi2)) {
       return getSpacingWithinBrackets(settings);
    }

    // [ [SPACE] ]
    if (psiElement(TT_OP_bracket_left).accepts(psi1) &&
        psiElement(TT_OP_bracket_right).accepts(psi2)) {
      return GosuSpaces.EMPTY;
    }

    return null;
  }
}
