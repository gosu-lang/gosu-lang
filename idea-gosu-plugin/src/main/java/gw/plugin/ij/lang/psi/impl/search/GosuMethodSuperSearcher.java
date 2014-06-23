/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.search;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.search.searches.SuperMethodsSearch;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.util.Processor;
import com.intellij.util.QueryExecutor;
import gw.plugin.ij.util.GosuProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuMethodSuperSearcher implements QueryExecutor<MethodSignatureBackedByPsiMethod, SuperMethodsSearch.SearchParameters> {
  private boolean find(@Nullable String getter, @Nullable String setter, @NotNull PsiClass klass, @NotNull final Processor<MethodSignatureBackedByPsiMethod> consumer) {
    for (PsiClass superKlass : klass.getSupers()) {
      for (PsiMethod method : superKlass.getMethods()) {
        final MethodSignatureBackedByPsiMethod signature = (MethodSignatureBackedByPsiMethod) method.getSignature(PsiSubstitutor.EMPTY);

        if (getter != null && getter.equals(GosuProperties.getGetterName(method))) {
           if(!consumer.process(signature)) {
             return false;
           }
        }

        if (setter != null && setter.equals(GosuProperties.getSetterName(method))) {
          if (!consumer.process(signature)) {
            return false;
          }
        }
      }

      if (!find(getter, setter, superKlass, consumer)) {
        return false;
      }
    }

    return true;
  }


  public boolean execute(@NotNull final SuperMethodsSearch.SearchParameters queryParameters, @NotNull final Processor<MethodSignatureBackedByPsiMethod> consumer) {
    final PsiMethod method = queryParameters.getMethod();
    final String getterName = GosuProperties.getGetterName(method);
    final String setterName = GosuProperties.getSetterName(method);

    if (getterName != null || setterName != null) {
      final PsiClass klass = method.getContainingClass();
      return klass == null || find(getterName, setterName, klass, consumer);
    }

    return true;
  }
}
