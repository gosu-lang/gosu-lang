/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.lang.psi.impl;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.DirectClassInheritorsSearch;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.intellij.util.QueryExecutor;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuAnonymousClassDefinition;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuReferenceList;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.stubs.index.GosuAnonymousClassIndex;
import gw.plugin.ij.lang.psi.stubs.index.GosuDirectInheritorsIndex;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ven
 */
public class GosuDirectInheritorSearcher implements QueryExecutor<PsiClass, DirectClassInheritorsSearch.SearchParameters> {
  public GosuDirectInheritorSearcher() {
  }

  @NotNull
  private static PsiClass[] getDeriverCandidates(@NotNull PsiClass clazz, GlobalSearchScope scope) {
    final String name = clazz.getName();
    if (name == null) return new PsiClass[0];
    final ArrayList<PsiClass> inheritors = new ArrayList<>();
    Collection<IGosuReferenceList> gosuReferenceLists = StubIndex.getInstance().get(GosuDirectInheritorsIndex.KEY, name, clazz.getProject(), scope);
    for (IGosuReferenceList list : gosuReferenceLists) {
      final PsiElement parent = list.getParent();
      if (parent instanceof IGosuTypeDefinition) {
        inheritors.add((IGosuTypeDefinition) parent);
      }
    }
    Collection<IGosuAnonymousClassDefinition> classes = StubIndex.getInstance().get(GosuAnonymousClassIndex.KEY, name, clazz.getProject(), scope);
    for (IGosuAnonymousClassDefinition aClass : classes) {
      inheritors.add(aClass);
    }
    return inheritors.toArray(new PsiClass[inheritors.size()]);
  }

  public boolean execute(@NotNull DirectClassInheritorsSearch.SearchParameters queryParameters, @NotNull final Processor<PsiClass> consumer) {
    final PsiClass clazz = queryParameters.getClassToProcess();
    final SearchScope scope = queryParameters.getScope();
    if (scope instanceof GlobalSearchScope) {
      final PsiClass[] candidates = ApplicationManager.getApplication().runReadAction(new Computable<PsiClass[]>() {
        @NotNull
        public PsiClass[] compute() {
          if (!clazz.isValid()) return PsiClass.EMPTY_ARRAY;
          return getDeriverCandidates(clazz, (GlobalSearchScope) scope);
        }
      });
      for (final PsiClass candidate : candidates) {
        final boolean isInheritor = ApplicationManager.getApplication().runReadAction(new Computable<Boolean>() {
          public Boolean compute() {
            return candidate.isInheritor(clazz, false);
          }
        });
        if (isInheritor) {
          if (!consumer.process(candidate)) {
            return false;
          }
        }
      }
      return true;
    }
    return true;
  }
}
