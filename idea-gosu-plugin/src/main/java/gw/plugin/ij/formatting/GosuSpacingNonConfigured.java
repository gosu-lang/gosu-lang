/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import gw.plugin.ij.lang.GosuCommentImpl;
import gw.plugin.ij.lang.GosuTokenSets;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;

public class GosuSpacingNonConfigured extends GosuElementTypes {
  private static final ElementPattern<PsiElement> DOT_IN_BEAN_METHOD_CALL_PATTERN = psiElement(TT_OP_dot)
      .withParent(psiElement(ELEM_TYPE_BeanMethodCallExpression));

  private static final ElementPattern<PsiElement> DOT_IN_MEMBER_ACCESS = psiElement(TT_OP_dot)
      .withParent(psiElement(ELEM_TYPE_MemberAccess));

  private static final ElementPattern<PsiElement> DOT_IN_TYPE_LITERAL = psiElement(TT_OP_dot)
      .withParent(psiElement(ELEM_TYPE_TypeLiteral));

  private static final ElementPattern<PsiElement> IN_STATEMENT_LIST = psiElement()
      .withParent(psiElement(ELEM_TYPE_StatementList));

  private static Spacing getSpacingAfterModifier() {
    return Spacing.createSafeSpacing(true, 0);
  }

  @Nullable
  public static Spacing getSpacing(PsiElement psi1, @NotNull PsiElement psi2, CommonCodeStyleSettings settings, GosuCodeStyleSettings gosuSettings) {
    // smth [SPACE] ;
    if (psi2.getText().equals(";")) {
      return GosuSpaces.EMPTY;
    }

    // new [SPACE] smth
    if (psiElement(TT_new)
        .withParent(psiElement(ELEM_TYPE_NewExpression))
        .accepts(psi1)) {
      return Spacing.createSafeSpacing(false, 0);
    }

    // smth [SPACE] in [SPACE] smth
    if (psiElement(TT_in).accepts(psi2) ||
        psiElement(TT_in).accepts(psi1)) {
      return Spacing.createSafeSpacing(false, 0);
    }

    // smth [SPACE] typeis [SPACE] smth
    if (psiElement(TT_typeis).accepts(psi2) ||
        psiElement(TT_typeis).accepts(psi1)) {
      return Spacing.createSafeSpacing(false, 0);
    }

    // smth [SPACE] as [SPACE] smth
    if (psiElement(TT_as).accepts(psi2) ||
        psiElement(TT_as).accepts(psi1)) {
      return Spacing.createSafeSpacing(false, 0);
    }

    // typeof [SPACE] smth
    if (psiElement(TT_typeof)
        .accepts(psi1)) {
      return Spacing.createSafeSpacing(false, 0);
    }

    // obj [SPACE] . [SPACE] method
    if (DOT_IN_BEAN_METHOD_CALL_PATTERN.accepts(psi2) ||
        DOT_IN_BEAN_METHOD_CALL_PATTERN.accepts(psi1) ||
        DOT_IN_MEMBER_ACCESS.accepts(psi2) ||
        DOT_IN_MEMBER_ACCESS.accepts(psi1) ||
        DOT_IN_TYPE_LITERAL.accepts(psi2) ||
        DOT_IN_TYPE_LITERAL.accepts(psi1)) {
      if(psi1 instanceof GosuCommentImpl) {
        return Spacing.createSafeSpacing(true, 0);
      }
      return GosuSpaces.getSpace(false, settings);
    }

    // package [SPACE] Identifier
    // class [SPACE] Identifier
    // interface [SPACE] Identifier
    // enhacement [SPACE] Identifier
    // enum [SPACE] Identifier
    // function [SPACE] Identifier
    // var [SPACE] Identifier
    // extends [SPACE] Class
    // implements [SPACE] Interface
    // return[SPACE] smth
    if (or(psiElement(TT_package),
        psiElement(TT_uses),
        psiElement(TT_class),
        psiElement(TT_interface),
        psiElement(TT_annotation),
        psiElement(TT_structure),
        psiElement(TT_enhancement),
        psiElement(TT_enum),
        psiElement(TT_function),
        psiElement(TT_var),
        psiElement(TT_extends),
        psiElement(TT_implements),
        psiElement(TT_return))
        .accepts(psi1)) {
      return Spacing.createSafeSpacing(false, 0);
    }

    // smth [SPACE] extends
    // smth [SPACE] implements
    if (or(psiElement(EXTENDS_CLAUSE),
        psiElement(IMPLEMENTS_CLAUSE))
        .accepts(psi2)) {
      return Spacing.createSafeSpacing(false, 0);
    }

    // Modifiers
    if (psiElement()
        .withElementType(GosuTokenSets.MODIFIERS)
        .accepts(psi1)) {
      return getSpacingAfterModifier();
    }

    //@ [SPACE] Annotation
    if (psiElement(TT_OP_at)
        .withParent(psiElement(ELEM_TYPE_AnnotationExpression))
        .accepts(psi1)) {
      return GosuSpaces.EMPTY;
    }

    // @Annotation [SPACE] *
    // * [SPACE] @Annotation
    if (psiElement(ELEM_TYPE_AnnotationExpression).accepts(psi1) ||
        psiElement(ELEM_TYPE_AnnotationExpression).accepts(psi2)) {
      return GosuSpaces.LINE_FEED;
    }

    // statement [SPACE] statement
    if (IN_STATEMENT_LIST.accepts(psi1) &&
        IN_STATEMENT_LIST.accepts(psi2)) {
      return GosuSpaces.getSpace(false, 1, settings);
    }

    return null;
  }
}
