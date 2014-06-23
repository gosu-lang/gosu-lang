/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import gw.internal.gosu.parser.expressions.CompoundTypeLiteral;
import gw.lang.parser.IParsedElement;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HandleInterfaceRedundantFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  private final IParsedElement parsedElement;

  public HandleInterfaceRedundantFix(@Nullable PsiElement element, IParsedElement pe) {
    super(element);
    this.parsedElement = pe;
  }


  @Override
  public void invoke(@NotNull Project project,
                     @NotNull PsiFile file,
                     @Nullable("is null when called from inspection") Editor editor,
                     @NotNull PsiElement startElement,
                     @NotNull PsiElement endElement) {

    if(!CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile()) ) {
      return;
    }
    if (parsedElement.getParent() instanceof CompoundTypeLiteral && editor != null) {
      int i = parsedElement.getLocation().getOffset();
      Document document = editor.getDocument();
      String text = document.getText();
      int s = text.substring(0, i).lastIndexOf("&");
      int length = parsedElement.getLocation().getLength();
      String part0 = text.substring(0, s);
      String part1 = text.substring(i + length);
      document.setText(part0 + part1);
      ((AbstractGosuClassFileImpl) file).reparseGosuFromPsi();
    }
  }

  @NotNull
  @Override
  public String getText() {
    return "Remove redundant interface";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }
}
