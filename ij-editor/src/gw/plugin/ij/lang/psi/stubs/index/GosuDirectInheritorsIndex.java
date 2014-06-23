/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuReferenceList;
import org.jetbrains.annotations.NotNull;

public class GosuDirectInheritorsIndex extends StringStubIndexExtension<IGosuReferenceList> {
  public static final StubIndexKey<String, IGosuReferenceList> KEY = StubIndexKey.createIndexKey("gosu.class.super");

  @NotNull
  public StubIndexKey<String, IGosuReferenceList> getKey() {
    return KEY;
  }
}