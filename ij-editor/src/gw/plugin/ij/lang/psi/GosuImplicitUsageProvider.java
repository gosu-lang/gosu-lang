/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi;

import com.intellij.codeInsight.daemon.ImplicitUsageProvider;
import com.intellij.psi.PsiElement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import org.jetbrains.annotations.NotNull;

/**
 */
public class GosuImplicitUsageProvider implements ImplicitUsageProvider {
  @Override
  public boolean isImplicitUsage(@NotNull PsiElement element) {
    return element.getNode() instanceof GosuCompositeElement;
  }

  @Override
  public boolean isImplicitRead(@NotNull PsiElement element) {
    return element.getNode() instanceof GosuCompositeElement;
  }

  @Override
  public boolean isImplicitWrite(@NotNull PsiElement element) {
    return element.getNode() instanceof GosuCompositeElement;
  }
}
