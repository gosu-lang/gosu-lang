/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;
import static gw.plugin.ij.formatting.GosuSpaces.LINE_FEED;

public class GosuBlankLinesProcessor extends GosuElementTypes {
  private static final ElementPattern<PsiElement> IS_METHOD = psiElement(METHOD_DEFINITION);
  private static final ElementPattern<PsiElement> IS_INTERFACE_METHOD = psiElement(METHOD_DEFINITION).withParent(psiElement(INTERFACE_DEFINITION));
  private static final ElementPattern<PsiElement> IS_FIELD = psiElement(FIELD);
  private static final ElementPattern<PsiElement> IS_INTERFACE_FIELD = psiElement(FIELD).withParent(psiElement(INTERFACE_DEFINITION));
  private static final ElementPattern<PsiElement> IS_ANONYMOUS_CLASS_DEFINITION = psiElement(ANONYMOUS_CLASS_DEFINITION);
  private static final ElementPattern<PsiElement> IS_NON_ANONYMOUS_CLASS_DEFINITION = or(
      psiElement(CLASS_DEFINITION),
      psiElement(INTERFACE_DEFINITION),
      psiElement(ENUM_DEFINITION),
      psiElement(ENHANCEMENT_DEFINITION),
      psiElement(ANNOTATION_DEFINITION));
  private static final ElementPattern<PsiElement> IS_NAMESPACE_STATEMENT = psiElement(ELEM_TYPE_NamespaceStatement);
  private static final ElementPattern<PsiElement> IS_USES_STATEMENT_LIST = psiElement(ELEM_TYPE_UsesStatementList);

  @Nullable
  private static Spacing getSpacingAroundPackage(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    // [LINES]
    // package Identifier
    if (IS_NAMESPACE_STATEMENT.accepts(psi2)) {
      return GosuSpaces.blankLines(settings.BLANK_LINES_BEFORE_PACKAGE);
    }

    // package Identifier
    // [LINES]
    if (IS_NAMESPACE_STATEMENT.accepts(psi1)) {
      return GosuSpaces.blankLines(settings.BLANK_LINES_AFTER_PACKAGE);
    }

    return null;
  }

  @Nullable
  private static Spacing getSpacingAroundUses(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    // [LINES]
    // uses a
    // uses b
    // uses c
    if (IS_USES_STATEMENT_LIST.accepts(psi2)) {
      return GosuSpaces.blankLines(settings.BLANK_LINES_BEFORE_IMPORTS);
    }

    // uses a
    // uses b
    // uses c
    // [LINES]
    if (IS_USES_STATEMENT_LIST.accepts(psi1)) {
      return GosuSpaces.blankLines(settings.BLANK_LINES_AFTER_IMPORTS);
    }

    return null;
  }

  @Nullable
  private static Spacing getSpacingAroundClass(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    if (IS_NON_ANONYMOUS_CLASS_DEFINITION.accepts(psi2)) {
      return psiElement(TT_OP_brace_left).accepts(psi1) ? LINE_FEED : GosuSpaces.blankLines(settings.BLANK_LINES_AROUND_CLASS);
    }

    if (IS_NON_ANONYMOUS_CLASS_DEFINITION.accepts(psi1)) {
      return psiElement(TT_OP_brace_right).accepts(psi2) ? LINE_FEED : GosuSpaces.blankLines(settings.BLANK_LINES_AROUND_CLASS);
    }

    return null;
  }

  @Nullable
  private static Spacing getSpacingAfterClassHeader(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    // class/interface/enum/enhancement/annotation Identifier {
    // [LINES]
    if (psiElement(TT_OP_brace_left).withParent(IS_NON_ANONYMOUS_CLASS_DEFINITION)
        .accepts(psi1)) {
      return GosuSpaces.blankLines(settings.BLANK_LINES_AFTER_CLASS_HEADER);
    }

    // new Identifier() {
    // [LINES]
    if (psiElement(TT_OP_brace_left).withParent(IS_ANONYMOUS_CLASS_DEFINITION)
        .accepts(psi1)) {
      return GosuSpaces.blankLines(settings.BLANK_LINES_AFTER_ANONYMOUS_CLASS_HEADER);
    }

    return null;
  }

  @Nullable
  private static Spacing getSpacingAroundHelper(@NotNull ElementPattern<PsiElement> pattern, PsiElement psi1, PsiElement psi2, int blankLines) {
    // Before
    if (pattern.accepts(psi2)) {
      return psiElement(TT_OP_brace_left).accepts(psi1) ? LINE_FEED : GosuSpaces.blankLines(blankLines);
    }

    // After
    if (pattern.accepts(psi1)) {
      return psiElement(TT_OP_brace_right).accepts(psi2) ? LINE_FEED : GosuSpaces.blankLines(blankLines);
    }

    return null;
  }

  @Nullable
  private static Spacing getSpacingAroundFields(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    Spacing spacing;

    // interface Identifier {
    //   var a: Type
    //   [LINES]
    //   var b: Type
    //   [LINES]
    //   var c: Type
    // }
    spacing = getSpacingAroundHelper(IS_INTERFACE_FIELD, psi1, psi2, settings.BLANK_LINES_AROUND_FIELD_IN_INTERFACE);
    if (spacing != null) {
      return spacing;
    }

    // class/enum/enhancement/annotation Identifier {
    //   function a() {}
    //   [LINES]
    //   function b() {}
    //   [LINES]
    //   function c() {}
    // }
    spacing = getSpacingAroundHelper(IS_FIELD, psi1, psi2, settings.BLANK_LINES_AROUND_FIELD);
    if (spacing != null) {
      return spacing;
    }

    return null;
  }

  @Nullable
  private static Spacing getSpacingAroundMethods(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    Spacing spacing;

    // interface Identifier {
    //   function a()
    //   [LINES]
    //   function b()
    //   [LINES]
    //   function c()
    // }
    spacing = getSpacingAroundHelper(IS_INTERFACE_METHOD, psi1, psi2, settings.BLANK_LINES_AROUND_METHOD_IN_INTERFACE);
    if (spacing != null) {
      return spacing;
    }

    // class/enum/enhancement/annotation Identifier {
    //   function a() {}
    //   [LINES]
    //   function b() {}
    //   [LINES]
    //   function c() {}
    // }
    spacing = getSpacingAroundHelper(IS_METHOD, psi1, psi2, settings.BLANK_LINES_AROUND_METHOD);
    if (spacing != null) {
      return spacing;
    }

    return spacing;
  }

  @Nullable
  private static Spacing getSpacingBeforeMethodBody(PsiElement psi1, PsiElement psi2, @NotNull CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    // function Identifier() {
    // [LINES]
    if (psiElement(TT_OP_brace_left)
        .withParent(psiElement(ELEM_TYPE_StatementList)
            .withParent(psiElement(METHOD_DEFINITION)))
        .accepts(psi1)) {
      return GosuSpaces.blankLines(settings.BLANK_LINES_BEFORE_METHOD_BODY);
    }

    if (psiElement(TT_OP_brace_right)
        .withParent(psiElement(ELEM_TYPE_StatementList))
        .accepts(psi2)) {
      return GosuSpaces.LINE_FEED;
    }

    return null;
  }

  @Nullable
  public static Spacing getSpacing(@NotNull GosuBlock child1, @NotNull GosuBlock child2, @NotNull CodeStyleSettings settings) {
    final PsiElement psi1 = child1.getNode().getPsi();
    final PsiElement psi2 = child2.getNode().getPsi();

    // No space before java doc
    if (psiElement(JavaDocElementType.DOC_COMMENT)
        .accepts(psi1)) {
      return GosuSpaces.LINE_FEED;
    }

    final CommonCodeStyleSettings commonSettings = settings.getCommonSettings(GosuLanguage.instance());
    final GosuCodeStyleSettings gosuSettings = settings.getCustomSettings(GosuCodeStyleSettings.class);

    Spacing spacing;
    if ((spacing = getSpacingAroundPackage(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = getSpacingAroundUses(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = getSpacingAroundClass(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = getSpacingAfterClassHeader(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = getSpacingAroundFields(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = getSpacingAroundMethods(psi1, psi2, commonSettings, gosuSettings)) != null ||
        (spacing = getSpacingBeforeMethodBody(psi1, psi2, commonSettings, gosuSettings)) != null) {
      return spacing;
    }

    return null;
  }
}
