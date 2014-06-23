/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Processor;
import com.intellij.util.containers.CollectionFactory;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.HashSet;
import gw.plugin.ij.lang.psi.impl.CustomPsiClassCache;
import gw.plugin.ij.lang.psi.impl.search.GosuSourceFilterScope;
import gw.plugin.ij.lang.psi.stubs.index.GosuClassNameIndex;
import gw.plugin.ij.lang.psi.stubs.index.GosuFieldNameIndex;
import gw.plugin.ij.lang.psi.stubs.index.GosuMethodNameIndex;
import gw.plugin.ij.lang.psi.stubs.index.GosuShortClassNameIndex;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class GosuShortNamesCache extends PsiShortNamesCache {
  private final Project myProject;

  public GosuShortNamesCache(Project project) {
    myProject = project;
  }

  @NotNull
  public PsiClass[] getClassesByName(@NotNull @NonNls String name, @NotNull GlobalSearchScope scope) {
    final ArrayList<PsiClass> allClasses = CollectionFactory.arrayList();
    Collection<PsiClass> gosuFiles = StubIndex.getInstance().get(GosuShortClassNameIndex.KEY, name, myProject, new GosuSourceFilterScope(scope));
    for (PsiClass file : gosuFiles) {
      ContainerUtil.addIfNotNull(file, allClasses);
    }

    try {
      gosuFiles = CustomPsiClassCache.instance().getByShortName(name);
    } catch (RuntimeException e) {
      // PL-28213.  Hack to short-circuit case where the file is not yet known by the type system.
      return PsiClass.EMPTY_ARRAY;
    }
    for (PsiClass file : gosuFiles) {
      ContainerUtil.addIfNotNull(file, allClasses);
    }

    if (allClasses.isEmpty()) {
      return PsiClass.EMPTY_ARRAY;
    } else {
      return allClasses.toArray(new PsiClass[allClasses.size()]);
    }
  }

  @NotNull
  private Collection<String> getAll() {
    Collection<String> all = StubIndex.getInstance().getAllKeys(GosuClassNameIndex.KEY, myProject);
    all.addAll(CustomPsiClassCache.instance().getAllClassNames());
    return all;
  }

  @NotNull
  public String[] getAllClassNames() {
    return ArrayUtil.toStringArray(getAll());
  }

  public void getAllClassNames(@NotNull HashSet<String> dest) {
    dest.addAll(getAll());
  }

  @NotNull
  public PsiMethod[] getMethodsByName(@NonNls @NotNull String name, @NotNull GlobalSearchScope scope) {
    final Collection<? extends PsiMethod> methods = StubIndex.getInstance().get(GosuMethodNameIndex.KEY, name, myProject, new GosuSourceFilterScope(scope));
    if (methods.isEmpty()) return PsiMethod.EMPTY_ARRAY;
    return methods.toArray(new PsiMethod[methods.size()]);
  }

  @NotNull
  public PsiMethod[] getMethodsByNameIfNotMoreThan(@NonNls @NotNull String name, @NotNull GlobalSearchScope scope, int maxCount) {
    return getMethodsByName(name, scope);
  }

  @NotNull
  @Override
  public PsiField[] getFieldsByNameIfNotMoreThan(@NonNls @NotNull String name, @NotNull GlobalSearchScope scope, int maxCount) {
    return new PsiField[0];
  }

  @Override
  public boolean processMethodsWithName(@NonNls @NotNull String s, @NotNull GlobalSearchScope globalSearchScope, @NotNull Processor<PsiMethod> psiMethodProcessor) {
    return false;
  }

  @NotNull
  public String[] getAllMethodNames() {
    Collection<String> keys = StubIndex.getInstance().getAllKeys(GosuMethodNameIndex.KEY, myProject);
    return ArrayUtil.toStringArray(keys);
  }

  public void getAllMethodNames(@NotNull HashSet<String> set) {
    set.addAll(StubIndex.getInstance().getAllKeys(GosuMethodNameIndex.KEY, myProject));
  }

  @NotNull
  public PsiField[] getFieldsByName(@NotNull @NonNls String name, @NotNull GlobalSearchScope scope) {
    final Collection<? extends PsiField> fields = StubIndex.getInstance().get(GosuFieldNameIndex.KEY, name, myProject, new GosuSourceFilterScope(scope));
    if (fields.isEmpty()) return PsiField.EMPTY_ARRAY;
    return fields.toArray(new PsiField[fields.size()]);
  }

  @NotNull
  public String[] getAllFieldNames() {
    Collection<String> fields = StubIndex.getInstance().getAllKeys(GosuFieldNameIndex.KEY, myProject);
    return ArrayUtil.toStringArray(fields);
  }

  public void getAllFieldNames(@NotNull HashSet<String> set) {
    set.addAll(StubIndex.getInstance().getAllKeys(GosuFieldNameIndex.KEY, myProject));
  }

}
