/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiMatcherImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBlockExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuNewExpressionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiMatchers.hasClass;

public class NewExpressionAsBlockFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  public NewExpressionAsBlockFix(GosuNewExpressionImpl newExpr) {
    super(newExpr);
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (!CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile())) {
      return;
    }
    if (!(startElement instanceof GosuNewExpressionImpl)) {
      return;
    }
    PsiElement method = new PsiMatcherImpl(startElement)
            .descendant(hasClass(GosuMethodImpl.class))
            .getElement();
    if (method == null) {
      return;
    }
    GosuMethodImpl gosuMethod = (GosuMethodImpl) method;
    String pars = gosuMethod.getParameterList().getText();
    String body = gosuMethod.getBody().getText();
    PsiElement[] children = gosuMethod.getBody().getChildren();
    if (children.length == 1) {
      String text = children[0].getText();
      int i = text.indexOf("return");
      if (i != -1) {
        body = text.substring(i + 6);
      }
    }
    String src = "\\ " + pars + " -> " + body;

    PsiElement stub = GosuPsiParseUtil.parseProgramm(src, method, file.getManager(), null);

    PsiElement block = new PsiMatcherImpl(stub)
            .descendant(hasClass(GosuBlockExpressionImpl.class))
            .getElement();
    if (block != null) {
      startElement.replace(block);
    }
  }

  @Override
  public boolean isAvailable(@NotNull Project project,
                             @NotNull PsiFile file,
                             @NotNull PsiElement startElement,
                             @NotNull PsiElement endElement) {
    return startElement instanceof GosuNewExpressionImpl;
  }

  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message("inspection.expression.as.block");
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message("inspection.group.name.expression.issues");
  }
}