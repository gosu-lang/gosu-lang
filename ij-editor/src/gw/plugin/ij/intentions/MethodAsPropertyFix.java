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
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiMatchers.hasClass;

public class MethodAsPropertyFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  private String methodName;
  private boolean isGetter;

  public MethodAsPropertyFix(IGosuMethod method, String methodName, boolean isGetter) {
    super(method);
    this.methodName = methodName;
    this.isGetter = isGetter;
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (!CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile())) {
      return;
    }
    if (!(startElement instanceof GosuMethodImpl)) {
      return;
    }
    String src = startElement.getText();
    String property = isGetter ? "property get" : "property set";
    String newMethodName = methodName.substring(3);
    src = src.replace("function", property);
    src = src.replaceFirst(methodName, newMethodName);
    PsiElement stub = GosuPsiParseUtil.parseProgramm(src, startElement, file.getManager(), null);

    PsiElement newMethod = new PsiMatcherImpl(stub)
            .descendant(hasClass(GosuMethodImpl.class))
            .getElement();
    startElement.replace(newMethod);
  }

  @Override
  public boolean isAvailable(@NotNull Project project,
                             @NotNull PsiFile file,
                             @NotNull PsiElement startElement,
                             @NotNull PsiElement endElement) {
    return startElement instanceof GosuMethodImpl;
  }

  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message("inspection.method.as.property");
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message("inspection.group.name.method.issues");
  }
}
