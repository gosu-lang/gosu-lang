/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.callhierarchy;

import com.intellij.ide.hierarchy.HierarchyBrowser;
import com.intellij.ide.hierarchy.call.JavaCallHierarchyProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public class GosuCallHierarchyProvider extends JavaCallHierarchyProvider {
  @NotNull
  @Override
  public HierarchyBrowser createHierarchyBrowser(PsiElement target) {
    return new GosuCallHierarchyBrowser(target.getProject(), (PsiMethod) target);
  }
}
