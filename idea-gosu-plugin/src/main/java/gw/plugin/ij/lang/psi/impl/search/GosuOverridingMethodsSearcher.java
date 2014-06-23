/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.search;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.search.searches.OverridingMethodsSearch;
import com.intellij.util.Processor;
import com.intellij.util.QueryExecutor;
import gw.plugin.ij.util.GosuProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuOverridingMethodsSearcher implements QueryExecutor<PsiMethod, OverridingMethodsSearch.SearchParameters> {
  public boolean execute(@NotNull final OverridingMethodsSearch.SearchParameters p, @NotNull final Processor<PsiMethod> consumer) {
    final PsiMethod method = p.getMethod();
    final SearchScope scope = p.getScope();

    final PsiClass parentClass = method.getContainingClass();
    assert parentClass != null;

    return ClassInheritorsSearch.search(parentClass, scope, true).forEach(new Processor<PsiClass>() {
      public boolean process(@NotNull final PsiClass inheritor) {
        PsiMethod found = ApplicationManager.getApplication().runReadAction(new Computable<PsiMethod>() {
          @Nullable
          public PsiMethod compute() {
            return findOverridingMethod(inheritor, parentClass, method);
          }
        });
        return found == null || consumer.process(found) && p.isCheckDeep();
      }
    });
  }

  @Nullable
  private static PsiMethod findOverridingMethod(@NotNull PsiClass inheritor, @NotNull PsiClass parentClass, PsiMethod method) {
    final String getterName = GosuProperties.getGetterName(method);
    if (getterName != null) {
      for(PsiMethod overriddenMethod : inheritor.getMethods()) {
        if (getterName.equals(GosuProperties.getGetterName(overriddenMethod ))) {
          return overriddenMethod;
        }
      }
    }

    final String setterName = GosuProperties.getSetterName(method);
    if (setterName != null) {
      for(PsiMethod overriddenMethod : inheritor.getMethods()) {
        if (setterName.equals(GosuProperties.getSetterName(overriddenMethod))) {
          return overriddenMethod;
        }
      }
    }

    return null;
  }
}
