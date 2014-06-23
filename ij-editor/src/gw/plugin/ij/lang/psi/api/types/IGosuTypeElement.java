/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.types;

import com.intellij.psi.PsiType;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import org.jetbrains.annotations.Nullable;

public interface IGosuTypeElement extends IGosuPsiElement {
  @Nullable
  PsiType getType();
}
