/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.google.common.base.Preconditions;
import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.daemon.QuickFixBundle;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiMatcherImpl;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.completion.GosuClassNameInsertHandler;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuParenthesizedExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeAsExpressionImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.util.PsiMatchers.hasClass;

public class GosuAddTypeCastFix extends BaseIntentionAction {
  private final PsiType type;
  private final IGosuPsiElement expression;

  public GosuAddTypeCastFix(PsiType type, IGosuPsiElement expression) {
    this.type = Preconditions.checkNotNull(type);
    this.expression = Preconditions.checkNotNull(expression);
  }

  @NotNull
  @Override
  public String getText() {
    return QuickFixBundle.message("add.typecast.text", type.getCanonicalText());
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return QuickFixBundle.message("add.typecast.family");
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return type.isValid() && expression.isValid() && expression.getManager().isInProject(expression);
  }

  @Override
  public void invokeImpl(@NotNull Project project, Editor editor, @NotNull PsiFile file) throws IncorrectOperationException {
    if (!CodeInsightUtilBase.prepareFileForWrite(file)) {
      return;
    }
    final PsiElement realExpression = expression instanceof GosuTypeAsExpressionImpl ? ((GosuTypeAsExpressionImpl) expression).getLhs() : expression;
    if(realExpression == null) {
      return;
    }
    String realExpressionTxt = realExpression.getText();
    final boolean surround = !(realExpression instanceof GosuParenthesizedExpressionImpl) && realExpression.getChildren().length > 0;
    String typeTxt = type.getPresentableText();
    String replacement = surround ? "(" + realExpressionTxt + ") as " + typeTxt : realExpressionTxt + " as " + typeTxt;

    PsiElement stub = GosuPsiParseUtil.parseProgramm("var a = " + replacement, realExpression, file.getManager(), null);
    PsiElement newExpr = new PsiMatcherImpl(stub).descendant(hasClass(GosuTypeAsExpressionImpl.class))
                                                 .getElement();
    if (newExpr != null) {
      expression.replace(newExpr);
      GosuClassNameInsertHandler.addImportForItem(file, type.getCanonicalText(), type.getPresentableText());
      ((AbstractGosuClassFileImpl) file).reparseGosuFromPsi();
    }
  }
}
