/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.reflect.gs.IGosuProgram;
import gw.plugin.ij.lang.GosuTokenImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuVariableImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class varInferenceFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  public varInferenceFix(PsiElement variable) {
    super(variable);
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (!CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile())) {
      return;
    }
    PsiElement firstChild = startElement.getFirstChild();
    if(firstChild == null) {
      return;
    }
    PsiElement nextSibling = firstChild.getNextSibling();
    ASTNode toRemoveStart = null;
    ASTNode toRemoveEnd = null;
    while (nextSibling != null) {
      if (nextSibling instanceof GosuTokenImpl) {
        GosuTokenImpl token = (GosuTokenImpl) nextSibling;
        if (token.textMatches(":")) {
          toRemoveStart = token.getNode();
        } else if (token.textMatches("=")) {
          toRemoveEnd = nextSibling.getNode();
          break;
        }
      }
      nextSibling = nextSibling.getNextSibling();
    }
    if (toRemoveStart != null && toRemoveEnd != null) {
      startElement.getNode().removeRange(toRemoveStart, toRemoveEnd);
    }
  }

  @Override
  public boolean isAvailable(@NotNull Project project,
                             @NotNull PsiFile file,
                             @NotNull PsiElement startElement,
                             @NotNull PsiElement endElement) {
    if(startElement instanceof GosuVariableImpl) {
      return true;
    } else if(startElement instanceof GosuFieldImpl) {
      GosuFieldImpl field = (GosuFieldImpl) startElement;
      IVarStatement parsedElement = field.getParsedElement();
      return parsedElement != null && (parsedElement.getGosuClass() instanceof IGosuProgram || parsedElement.isPrivate());

    }
    return false;
  }

  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message("inspection.variable.type.inferred");
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message("inspection.group.name.declaration.issues");
  }
}
