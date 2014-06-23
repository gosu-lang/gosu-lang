/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.expressions.ConditionalAndExpression;
import gw.internal.gosu.parser.expressions.EqualityExpression;
import gw.internal.gosu.parser.expressions.UnaryNotPlusMinusExpression;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBeanMethodCallExpressionImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ObjectEqualsAsOpFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  public ObjectEqualsAsOpFix(GosuBeanMethodCallExpressionImpl callExpression) {
    super(callExpression);
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (!CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile())) {
      return;
    }
    if (!(startElement instanceof GosuBeanMethodCallExpressionImpl)) {
      return;
    }
    IBeanMethodCallExpression parsedElement = ((GosuBeanMethodCallExpressionImpl) startElement).getParsedElement();
    if (parsedElement == null) {
      return;
    }
    IExpression[] args = parsedElement.getArgs();
    String root = parsedElement.getRootExpression().getLocation().getTextFromTokens();
    if (args != null && args.length == 1) {
      PsiElement toRemove = startElement;
      String op = " == ";
      IParsedElement expr = parsedElement.getParent();
      if (expr instanceof UnaryNotPlusMinusExpression) {
        UnaryNotPlusMinusExpression unaryExpr = (UnaryNotPlusMinusExpression) expr;
        if(unaryExpr.isNot()) {
          toRemove = startElement.getParent();
          op =  " != ";
        }
      }
      if (expr instanceof ConditionalAndExpression) {
        ConditionalAndExpression condExpr = (ConditionalAndExpression) expr;
        Expression lhs = condExpr.getLHS();
        if (lhs instanceof EqualityExpression) {
          EqualityExpression eq = (EqualityExpression) lhs;
          if (eq.getLHS().getLocation().getTextFromTokens().equals(root) &&
              eq.getRHS().getLocation().getTextFromTokens().equals("null") &&
              !eq.isEquals()) {
            toRemove = startElement.getParent();
          }
        }
      }
      String src = root + op + args[0].getLocation().getTextFromTokens();
      Document document = toRemove.getContainingFile().getViewProvider().getDocument();
      if(document != null) {
        int i = toRemove.getTextOffset();
        String text = document.getText();
        String newText = text.substring( 0, i ) + src + text.substring( i+toRemove.getTextLength());
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
        document.setText( newText );
      }

      if (file instanceof AbstractGosuClassFileImpl) {
        ((AbstractGosuClassFileImpl) file).reparsePsiFromContent();
      }
    }
  }

  @Override
  public boolean isAvailable(@NotNull Project project,
                             @NotNull PsiFile file,
                             @NotNull PsiElement startElement,
                             @NotNull PsiElement endElement) {
    return startElement instanceof GosuBeanMethodCallExpressionImpl;
  }

  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message("inspection.object.equals.as.op");
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message("inspection.group.name.expression.issues");
  }
}
