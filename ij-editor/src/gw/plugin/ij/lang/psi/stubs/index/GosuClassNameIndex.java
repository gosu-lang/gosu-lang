/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import gw.plugin.ij.lang.psi.IGosuFile;
import org.jetbrains.annotations.NotNull;

public class GosuClassNameIndex extends StringStubIndexExtension<IGosuFile> {
  public static final StubIndexKey<String, IGosuFile> KEY = StubIndexKey.createIndexKey("gosu.class");

  @NotNull
  public StubIndexKey<String, IGosuFile> getKey() {
    return KEY;
  }
}
