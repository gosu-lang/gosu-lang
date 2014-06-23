/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.index;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;

public class GosuAnnotatedMemberIndex extends StringStubIndexExtension<PsiElement> {
  public static final StubIndexKey<String, PsiElement> KEY = StubIndexKey.createIndexKey("gosu.annot.members");

  @NotNull
  public StubIndexKey<String, PsiElement> getKey() {
    return KEY;
  }
}