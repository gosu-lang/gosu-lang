/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiResolveHelper;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaPsiFacadeUtil
{
  private static JavaPsiFacade facade( Project project ) {
    return JavaPsiFacade.getInstance(project);
  }

  @Nullable
  public static PsiPackage findPackage(Project project, @NotNull String packageName) {
    return facade(project).findPackage(packageName);
  }

  @Nullable
  public static PsiClass findClass(Project project, @NotNull String qualifiedName, @NotNull GlobalSearchScope scope) {
    return facade(project).findClass(qualifiedName, scope);
  }

  @NotNull
  public static PsiClass[] findClasses(Project project, @NotNull String qualifiedName, @NotNull GlobalSearchScope scope) {
    return facade(project).findClasses(qualifiedName, scope);
  }

  @NotNull
  public static PsiElementFactory getElementFactory(Project project) {
    return facade(project).getElementFactory();
  }

  @NotNull
  public static PsiResolveHelper getResolveHelper(Project project) {
    return facade(project).getResolveHelper();
  }
}
