/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef.members;

import com.intellij.codeInsight.daemon.impl.analysis.HighlightVisitorImpl;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.impl.light.LightElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NotNull;

public class ThrowsReferenceList extends LightElement implements PsiReferenceList {
  private final int _iImaginaryStartOffset;

  public ThrowsReferenceList(PsiManager manager, int iImaginaryStartOffset ) {
    super(manager, StdFileTypes.JAVA.getLanguage());
    _iImaginaryStartOffset = iImaginaryStartOffset;
  }

  @NotNull
  public String toString() {
    return "ThrowsReferenceList";
  }

  @NotNull
  public String getText() {
    return "";
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JavaElementVisitor && !(visitor instanceof HighlightVisitorImpl) ) {
      ((JavaElementVisitor) visitor).visitReferenceList(this);
    } else if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitThrowsReferenceList(this);
    } else {
      visitor.visitElement(this);
    }
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    return new TextRange( _iImaginaryStartOffset, _iImaginaryStartOffset );
  }

  @NotNull
  public PsiElement copy() {
    return this;
  }

  @NotNull
  public PsiJavaCodeReferenceElement[] getReferenceElements() {
    return PsiJavaCodeReferenceElement.EMPTY_ARRAY;
  }

  @NotNull
  public PsiClassType[] getReferencedTypes() {
    return PsiClassType.EMPTY_ARRAY;
  }

  @NotNull
  public Role getRole() {
    return Role.THROWS_LIST;
  }
}
