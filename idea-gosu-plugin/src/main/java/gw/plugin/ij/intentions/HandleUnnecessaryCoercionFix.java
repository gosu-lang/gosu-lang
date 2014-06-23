/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.impl.expressions.GosuParenthesizedExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeAsExpressionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HandleUnnecessaryCoercionFix extends LocalQuickFixAndIntentionActionOnPsiElement {

  public HandleUnnecessaryCoercionFix(@Nullable PsiElement element) {
    super(element);
  }


  @Override
  public void invoke(@NotNull Project project,
                     @NotNull PsiFile file,
                     @Nullable("is null when called from inspection") Editor editor,
                     @NotNull PsiElement startElement,
                     @NotNull PsiElement endElement) {
    if( !CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile()) ) {
      return;
    }
    PsiElement typeAsExpr = getTypeAsExpr(startElement);
    if(typeAsExpr.getChildren().length == 1) {
      typeAsExpr = getTypeAsExpr(typeAsExpr.getParent());
    }
    if (typeAsExpr instanceof GosuTypeAsExpressionImpl  && editor != null) {
      PsiElement replaceMe;
      IGosuPsiElement lhs = ((GosuTypeAsExpressionImpl) typeAsExpr).getLhs();
      if(((GosuTypeAsExpressionImpl) typeAsExpr).getRhs() == null) {
        typeAsExpr = typeAsExpr.getParent();
      }
      if (typeAsExpr.getParent() instanceof GosuParenthesizedExpressionImpl) {
        typeAsExpr = typeAsExpr.getParent();
      }
      replaceMe = typeAsExpr;
      replaceMe.replace(lhs);
    }
  }

  private PsiElement getTypeAsExpr(PsiElement startElement) {
    return PsiTreeUtil.findFirstParent(startElement, new Condition<PsiElement>() {
      @Override
      public boolean value(PsiElement psiElement) {
        return psiElement instanceof GosuTypeAsExpressionImpl;
      }
    });
  }

  @NotNull
  @Override
  public String getText() {
    return "Remove unnecessary cast";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }
}
