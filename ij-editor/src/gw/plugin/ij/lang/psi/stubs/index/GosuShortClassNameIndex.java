/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import gw.plugin.ij.lang.psi.impl.search.GosuSourceFilterScope;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GosuShortClassNameIndex extends StringStubIndexExtension<PsiClass> {
  public static final StubIndexKey<String,PsiClass> KEY = StubIndexKey.createIndexKey("gosu.class.shortname");

  private static final GosuShortClassNameIndex ourInstance = new GosuShortClassNameIndex();
  @NotNull
  public static GosuShortClassNameIndex getInstance() {
    return ourInstance;
  }

  @NotNull
  public StubIndexKey<String, PsiClass> getKey() {
    return KEY;
  }

  public Collection<PsiClass> get(final String s, final Project project, @NotNull final GlobalSearchScope scope) {
    return super.get(s, project, new GosuSourceFilterScope(scope));
  }

}