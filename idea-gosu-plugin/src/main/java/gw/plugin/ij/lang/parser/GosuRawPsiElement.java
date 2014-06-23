/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GosuRawPsiElement extends GosuBaseElementImpl implements IGosuPsiElement, PsiModifierListOwner {
  public GosuRawPsiElement(GosuCompositeElement node) {
    super(node);
  }

  public void accept(GosuElementVisitor visitor) {
  }

  // PsiModifierListOwner
  @Override
  public PsiModifierList getModifierList() {
    return findChildByClass(PsiModifierList.class);
  }

  @Override
  public boolean hasModifierProperty(@PsiModifier.ModifierConstant @NonNls @NotNull String name) {
    final PsiModifierListOwner child = findChildByClass(PsiModifierListOwner.class);
    return child != null && child.hasModifierProperty(name);
  }

  @NotNull
  public String toString() {
    return "[raw] " + super.toString();
  }
}
