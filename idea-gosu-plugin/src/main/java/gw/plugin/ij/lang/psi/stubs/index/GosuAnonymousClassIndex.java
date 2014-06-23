/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuAnonymousClassDefinition;
import org.jetbrains.annotations.NotNull;

public class GosuAnonymousClassIndex extends StringStubIndexExtension<IGosuAnonymousClassDefinition> {
  public static final StubIndexKey<String, IGosuAnonymousClassDefinition> KEY = StubIndexKey.createIndexKey("gosu.anonymous.class");

  @NotNull
  public StubIndexKey<String, IGosuAnonymousClassDefinition> getKey() {
    return KEY;
  }
}
