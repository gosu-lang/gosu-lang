/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi;

import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.Nullable;

public interface IGosuNamedElement extends PsiNameIdentifierOwner, IGosuPsiElement {
  @Nullable
  @Override
  PsiIdentifier getNameIdentifier();
}
