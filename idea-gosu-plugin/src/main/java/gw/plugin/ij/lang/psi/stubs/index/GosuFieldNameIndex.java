/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import org.jetbrains.annotations.NotNull;

public class GosuFieldNameIndex extends StringStubIndexExtension<IGosuField> {
  public static final StubIndexKey<String, IGosuField> KEY = StubIndexKey.createIndexKey("gosu.field.name");

  @NotNull
  public StubIndexKey<String, IGosuField> getKey() {
    return KEY;
  }
}
