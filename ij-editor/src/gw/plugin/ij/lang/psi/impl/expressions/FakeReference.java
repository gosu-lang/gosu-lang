/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class FakeReference implements PsiReference {
  private final GosuIdentifierImpl identifier;
  private final PsiElement reference;

  public FakeReference(GosuIdentifierImpl id, PsiElement reference) {
    this.identifier = id;
    this.reference = reference;
  }

  @Override
  public PsiElement getElement() {
    return identifier;
  }

  @NotNull
  @Override
  public TextRange getRangeInElement() {
    return new TextRange(0, identifier.getTextLength());
  }

  @Override
  public PsiElement resolve() {
    return reference;
  }

  @Override
  public boolean isReferenceTo(@NotNull PsiElement element) {
    return PsiManagerImpl.getInstance(element.getProject()).areElementsEquivalent(reference, element);
  }

  @NotNull
  @Override
  public String getCanonicalText() {
    throw new RuntimeException("Not implemented");
  }

  @NotNull
  @Override
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    return reference;
  }

  @NotNull
  @Override
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    throw new RuntimeException("Not implemented");
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    return new Object[0];
  }

  @Override
  public boolean isSoft() {
    throw new RuntimeException("Not implemented");
  }
}
