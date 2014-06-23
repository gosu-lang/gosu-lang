/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.impl.expressions.GosuParenthesizedExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeAsExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.StandardPatterns.or;

public class GosuChangeTypeCastFix extends BaseIntentionAction {
  private final GosuTypeAsExpressionImpl expression;

  public GosuChangeTypeCastFix(GosuTypeAsExpressionImpl expression) {
    this.expression = expression;
  }

  @NotNull
  @Override
  public String getText() {
    return "Change Java-style cast to Gosu-style cast";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return expression.isValid() && expression.getManager().isInProject(expression);
  }

  @Override
  public void invokeImpl(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {

    final GosuParenthesizedExpressionImpl originalTypeExpr = PsiTreeUtil.getChildOfType(expression, GosuParenthesizedExpressionImpl.class);
    final GosuTypeLiteralImpl originalType = PsiTreeUtil.getChildOfType(originalTypeExpr, GosuTypeLiteralImpl.class);
    final PsiElement originalExpr = PsiTreeUtil.skipSiblingsForward(originalTypeExpr, PsiWhiteSpace.class);

    final PsiElement parent = expression.getParent();

    final boolean surround = or(
        psiElement(GosuElementTypes.ELEM_TYPE_TypeAsExpression),
        psiElement(GosuElementTypes.ELEM_TYPE_TypeOfExpression)).accepts(parent);

    final String template = surround ? "(1 as String)" : "1 as String";
    final PsiElement fakeExpression = GosuPsiParseUtil.parseExpression(template, expression.getManager());
    final PsiElement[]  fakeExpressionChildren= surround ? fakeExpression.getChildren()[0].getChildren() : fakeExpression.getChildren();
    fakeExpressionChildren[0].replace(originalExpr);
    fakeExpressionChildren[1].replace(originalType);

    expression.replace(fakeExpression);
  }
}