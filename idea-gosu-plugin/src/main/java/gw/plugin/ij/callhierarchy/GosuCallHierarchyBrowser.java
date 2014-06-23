/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.callhierarchy;

import com.intellij.ide.hierarchy.HierarchyTreeStructure;
import com.intellij.ide.hierarchy.call.CallHierarchyBrowser;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public class GosuCallHierarchyBrowser extends CallHierarchyBrowser {
  public GosuCallHierarchyBrowser(@NotNull Project project, @NotNull PsiMethod method) {
    super(project, method);
  }

  @Override
  protected HierarchyTreeStructure createHierarchyTreeStructure(@NotNull String typeName, @NotNull PsiElement psiElement) {
    if (CALLER_TYPE.equals(typeName)) {
      return new GosuCallerMethodsTreeStructure(myProject, (PsiMethod) psiElement, getCurrentScopeType());
    } else if (CALLEE_TYPE.equals(typeName)) {
      return new GosuCalleeMethodsTreeStructure(myProject, (PsiMethod) psiElement, getCurrentScopeType());
    } else {
      return null;
    }
  }
}
