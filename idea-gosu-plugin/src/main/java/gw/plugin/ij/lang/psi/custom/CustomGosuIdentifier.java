/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.custom;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.light.LightIdentifier;
import org.jetbrains.annotations.NotNull;

public class CustomGosuIdentifier extends LightIdentifier {
  @NotNull
  private final PsiElement element;

  public CustomGosuIdentifier(@NotNull PsiElement element) {
    super(element.getManager(), element.getText());
    this.element = element;
  }

  @Override
  public boolean isPhysical() {
    return true;
  }

  @Override
  public TextRange getTextRange() {
    return element.getTextRange();
  }

  public PsiFile getContainingFile() {
    return element.getContainingFile();
  }

}
