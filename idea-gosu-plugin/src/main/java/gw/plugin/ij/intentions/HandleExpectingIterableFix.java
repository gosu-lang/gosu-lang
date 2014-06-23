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
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HandleExpectingIterableFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  public HandleExpectingIterableFix( PsiElement id ) {
    super( id );
  }

  @Override
  public void invoke( @NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement ) {
    if (!CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile())) {
      return;
    }
    String src;
    if(startElement.getChildren().length == 0) {
      src = "0..|" + startElement.getText();
    } else {
      src = "0..|(" + startElement.getText() + ")";
    }
    Document document = startElement.getContainingFile().getViewProvider().getDocument();
    if(document != null) {
      int i = startElement.getTextOffset();
      String text = document.getText();
      String newText = text.substring( 0, i ) + src + text.substring( i+startElement.getTextLength());
      PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
      document.setText( newText );
    }

    if (file instanceof AbstractGosuClassFileImpl) {
      ((AbstractGosuClassFileImpl) file).reparsePsiFromContent();
    }

  }
  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message("inspection.expecting.iterable");
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message( "inspection.group.name.upgrade.issues" );
  }
}