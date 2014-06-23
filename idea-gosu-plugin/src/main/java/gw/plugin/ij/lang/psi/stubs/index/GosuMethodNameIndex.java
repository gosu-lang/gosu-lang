/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import org.jetbrains.annotations.NotNull;

public class GosuMethodNameIndex extends StringStubIndexExtension<IGosuMethod> {
  public static final StubIndexKey<String, IGosuMethod> KEY = StubIndexKey.createIndexKey("Gosu.method.name");

  @NotNull
  public StubIndexKey<String, IGosuMethod> getKey() {
    return KEY;
  }
}