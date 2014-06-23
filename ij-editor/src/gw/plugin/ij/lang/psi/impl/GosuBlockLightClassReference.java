/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightClassReference;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GosuBlockLightClassReference extends LightClassReference {
  public GosuBlockLightClassReference(@NotNull PsiManager manager, @NotNull @NonNls String text, @NotNull @NonNls String className, @NotNull GlobalSearchScope resolveScope) {
    super(manager, text, className, resolveScope);
  }

  @Override
  public String getReferenceName() {
    return getQualifiedName();
  }
}
