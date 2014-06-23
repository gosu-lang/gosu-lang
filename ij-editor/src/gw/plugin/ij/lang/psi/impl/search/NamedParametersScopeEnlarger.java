/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.search;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.UseScopeEnlarger;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

public class NamedParametersScopeEnlarger extends UseScopeEnlarger {

  @Override
  public SearchScope getAdditionalUseScope(@NotNull PsiElement element) {
    if (element instanceof IGosuParameter) {
      IModule module = GosuModuleUtil.findModuleForPsiElement(element);
      if (module != null) {
        Module m = GosuModuleUtil.getModule(module);
        if(m != null) {
          return GlobalSearchScope.moduleWithDependenciesScope(m);
        }
      }
      return GlobalSearchScope.projectScope(element.getProject());
    } else {
      return null;
    }
  }

}
