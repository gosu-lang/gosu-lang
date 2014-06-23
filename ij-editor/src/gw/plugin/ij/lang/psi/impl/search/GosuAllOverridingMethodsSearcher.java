/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.search;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AllOverridingMethodsSearch;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.Processor;
import com.intellij.util.QueryExecutor;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;
import gw.plugin.ij.util.GosuProperties;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class GosuAllOverridingMethodsSearcher implements QueryExecutor<Pair<PsiMethod, PsiMethod>, AllOverridingMethodsSearch.SearchParameters> {
  @Override
  public boolean execute(@NotNull AllOverridingMethodsSearch.SearchParameters p, @NotNull final Processor<Pair<PsiMethod, PsiMethod>> consumer) {
    // Parameters
    final PsiClass psiClass = p.getPsiClass();
    final SearchScope scope = p.getScope();

    // Setters and getters that can be overriden
    final List<PsiMethod> methods = new ArrayList<>();
    for (PsiMethod method : psiClass.getMethods()) {
      if (PsiUtil.canBeOverriden(method) && (GosuProperties.isSetter(method) || GosuProperties.isGetter(method))) {
        methods.add(method);
      }
    }

    // Search all inheritors
    return ClassInheritorsSearch.search(psiClass, scope, true).forEach(new Processor<PsiClass>() {
      public boolean process(@NotNull PsiClass inheritor) {
        //could be null if not java inheritor, TODO only JavaClassInheritors are needed
        final PsiSubstitutor substitutor = TypeConversionUtil.getClassSubstitutor(psiClass, inheritor, PsiSubstitutor.EMPTY);
        if (substitutor == null) {
          return true;
        }

        for (PsiMethod method : methods) {
          if ((method.hasModifierProperty(PsiModifier.PACKAGE_LOCAL) ||
               method.hasModifierProperty( IGosuModifier.INTERNAL)) &&
              !JavaPsiFacade.getInstance(inheritor.getProject()).arePackagesTheSame(psiClass, inheritor)) {
            continue;
          }

          final String getterName = GosuProperties.getGetterName(method);
          final String setterName = GosuProperties.getSetterName(method);

          for (PsiMethod overriddenMethod : inheritor.getMethods()) {
            if ((getterName != null && getterName.equals(GosuProperties.getGetterName(overriddenMethod))) ||
                (setterName != null && setterName.equals(GosuProperties.getSetterName(overriddenMethod)))) {
              if (!consumer.process(new Pair(method, overriddenMethod))) {
                return false;
              }
            }
          }
        }
        return true;
      }
    });
  }
}
