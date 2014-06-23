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
import com.intellij.psi.PsiIdentifier;
import gw.plugin.ij.lang.GosuTokenImpl;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CaseMismatchSimpleQuickFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  private String _correctName;

  public CaseMismatchSimpleQuickFix( PsiElement id, String correctName ) {
    super( id );
    _correctName = correctName;
  }

  @Override
  public void invoke( @NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement ) {
    if( !CodeInsightUtilBase.prepareFileForWrite( startElement.getContainingFile() ) ) {
      return;
    }
    int i = startElement.getTextOffset();
    Document document = startElement.getContainingFile().getViewProvider().getDocument();
    String text = document.getText();
    String newText = text.substring( 0, i ) + _correctName + text.substring( i + _correctName.length() );
    document.setText( newText );
    if( file instanceof AbstractGosuClassFileImpl ) {
      ((AbstractGosuClassFileImpl) file).reparsePsiFromContent();
    }
  }

  private PsiElement findIdentifierOrToken( PsiElement psi ) {
    PsiElement save = psi;
    if( !(psi instanceof PsiIdentifier) ) {
      psi = GosuBaseElementImpl.findElement( psi, PsiIdentifier.class );
    }
    if( psi == null ) {
      psi = save;
      psi = GosuBaseElementImpl.findElement( psi, GosuTokenImpl.class );
    }
    return psi;
  }

  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message( "inspection.case.mismatch" );
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message( "inspection.group.name.case.mismatch.issues" );
  }
}
