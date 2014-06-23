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
import gw.plugin.ij.lang.GosuTokenImpl;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EqualityQuickFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  public EqualityQuickFix( PsiElement id ) {
    super( id );
  }

  @Override
  public void invoke( @NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement ) {
    if( !CodeInsightUtilBase.prepareFileForWrite( startElement.getContainingFile() ) ) {
      return;
    }
    ((GosuTokenImpl) startElement).replaceWithText("!=");
    if( file instanceof AbstractGosuClassFileImpl ) {
      ((AbstractGosuClassFileImpl) file).reparsePsiFromContent();
    }
  }
  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message( "inspection.equality" );
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message( "inspection.group.name.equality.issues" );
  }
}
