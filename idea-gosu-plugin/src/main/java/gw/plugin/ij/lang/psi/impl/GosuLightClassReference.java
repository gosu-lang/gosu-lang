/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.lang.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightClassReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GosuLightClassReference extends LightClassReference {
  private final GosuIdentifierImpl namedElement;

  public GosuLightClassReference(
      @NotNull PsiManager manager, @NotNull @NonNls String text,
      @NotNull @NonNls String className, @NotNull GlobalSearchScope resolveScope, GosuIdentifierImpl namedElement) {
    super(manager, text, className, resolveScope);
    this.namedElement = namedElement;
  }

  public PsiElement getReferenceNameElement() {
    return namedElement;
  }

  public PsiFile getContainingFile(){
    return namedElement.getContainingFile();
  }

  @NotNull
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    GosuTypeLiteralImpl parent = (GosuTypeLiteralImpl) namedElement.getParent();
    return parent.bindToElement(element);
  }
}
