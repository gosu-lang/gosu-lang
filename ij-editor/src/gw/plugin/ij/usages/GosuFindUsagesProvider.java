/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.usages;

import com.intellij.lang.java.JavaFindUsagesProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class GosuFindUsagesProvider extends JavaFindUsagesProvider {
  public GosuFindUsagesProvider() {
  }

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement element) {
    return super.canFindUsagesFor(element);
  }

  @Override
  public String getHelpId(@NotNull PsiElement element) {
    return super.getHelpId(element);
  }

  @NotNull
  @Override
  public String getType(@NotNull PsiElement element) {
    return super.getType(element);
  }

  @NotNull
  @Override
  public String getDescriptiveName(@NotNull PsiElement element) {
    return super.getDescriptiveName(element);
  }

  @NotNull
  @Override
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    return super.getNodeText(element, useFullName);
  }
}
