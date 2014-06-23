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
import com.intellij.psi.PsiIdentifier;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CaseMismatchRelativeTypeQuickFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  private String _correctName;

  public CaseMismatchRelativeTypeQuickFix( PsiElement id, String correctName ) {
    super( id );
    _correctName = correctName;
  }

  @Override
  public void invoke( @NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement ) {
    if( !CodeInsightUtilBase.prepareFileForWrite( startElement.getContainingFile() ) ) {
      return;
    }
    PsiElement psiIdentifier = GosuPsiParseUtil.parseRelativeTypeLiteral( _correctName, startElement ).getFirstChild();
    if( !(startElement instanceof PsiIdentifier) ) {
      startElement = GosuBaseElementImpl.findElement( startElement, PsiIdentifier.class );
    }
    startElement.replace( psiIdentifier );
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
