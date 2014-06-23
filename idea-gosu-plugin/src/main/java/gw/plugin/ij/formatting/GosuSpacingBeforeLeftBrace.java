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

public class GosuSpacingBeforeLeftBrace extends GosuElementTypes {
  // Before left brace
  private static Spacing getSpacingBeforeClassLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_CLASS_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeFunctionLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_METHOD_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeIfLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_IF_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeElseLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_ELSE_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeForEachLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_FOR_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeWhileLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_WHILE_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeDoWhileLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_DO_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeSwitchLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_SWITCH_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeTryLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_TRY_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeCatchLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_CATCH_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeFinallyLeftBrace(@NotNull CommonCodeStyleSettings settings) {
    return GosuSpaces.getSpace(settings.SPACE_BEFORE_FINALLY_LBRACE, settings);
  }

  private static Spacing getSpacingBeforeUsingLeftBrace(@NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    return GosuSpaces.getSpace(gosuSettings.SPACE_BEFORE_USING_LBRACE, settings);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, @NotNull GosuCodeStyleSettings gosuSettings) {
    // class/interface/enhancement/enum Indentifier [SPACE] {
    if (psiElement(TT_OP_brace_left)
        .withParent(GosuIndentProcessor.ALL_DEFINITIONS)
        .accepts(psi2)) {
      return getSpacingBeforeClassLeftBrace(settings);
    }

    // function Indentifier () [SPACE] {
    if (psiElement(ELEM_TYPE_StatementList)
        .withParent(psiElement(METHOD_DEFINITION))
        .accepts(psi2)) {
      return getSpacingBeforeFunctionLeftBrace(settings);
    }

    // if () [SPACE] {
    if (psiElement(ELEM_TYPE_StatementList)
        .withParent(psiElement(ELEM_TYPE_IfStatement))
        .beforeLeaf(psiElement(TT_else))
        .accepts(psi2)) {
      return getSpacingBeforeIfLeftBrace(settings);
    }

    // else [SPACE] {
    if (psiElement(ELEM_TYPE_StatementList)
        .withParent(psiElement(ELEM_TYPE_IfStatement))
        .afterLeaf(psiElement(TT_else))
        .accepts(psi2)) {
      return getSpacingBeforeElseLeftBrace(settings);
    }

    // for () [SPACE] {
    if (psiElement(ELEM_TYPE_StatementList)
        .withParent(psiElement(ELEM_TYPE_ForEachStatement))
        .accepts(psi2)) {
      return getSpacingBeforeForEachLeftBrace(settings);
    }

    // while () [SPACE] {
    if (psiElement(ELEM_TYPE_StatementList)
        .withParent(psiElement(ELEM_TYPE_WhileStatement))
        .accepts(psi2)) {
      return getSpacingBeforeWhileLeftBrace(settings);
    }

    // do () [SPACE] {
    if (psiElement(ELEM_TYPE_StatementList)
        .withParent(psiElement(ELEM_TYPE_DoWhileStatement))
        .accepts(psi2)) {
      return getSpacingBeforeDoWhileLeftBrace(settings);
    }

    // switch () [SPACE] {
    if (psiElement(TT_OP_brace_left)
        .withParent(psiElement(ELEM_TYPE_SwitchStatement))
        .accepts(psi2)) {
      return getSpacingBeforeSwitchLeftBrace(settings);
    }

    // try [SPACE] {
    if (psiElement(TT_try).accepts(psi1) &&
        psiElement(ELEM_TYPE_StatementList).accepts(psi2)) {
      return getSpacingBeforeTryLeftBrace(settings);
    }

    // catch () [SPACE] {
    if (psiElement(ELEM_TYPE_StatementList)
        .withParent(psiElement(ELEM_TYPE_CatchClause))
        .accepts(psi2)) {
      return getSpacingBeforeCatchLeftBrace(settings);
    }

    // finally [SPACE] {
    if (psiElement(TT_finally).accepts(psi1) &&
        psiElement(ELEM_TYPE_StatementList).accepts(psi2)) {
      return getSpacingBeforeFinallyLeftBrace(settings);
    }

    // using() [SPACE] {
    if (psiElement(ELEM_TYPE_StatementList)
        .withParent(psiElement(ELEM_TYPE_UsingStatement))
        .accepts(psi2)) {
      return getSpacingBeforeUsingLeftBrace(settings, gosuSettings);
    }

    return null;
  }
}
