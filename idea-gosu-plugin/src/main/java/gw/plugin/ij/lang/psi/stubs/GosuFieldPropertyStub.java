/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs;

import com.intellij.psi.stubs.NamedStub;
import gw.plugin.ij.lang.psi.api.statements.IGosuFieldProperty;
import org.jetbrains.annotations.NotNull;

public interface GosuFieldPropertyStub extends NamedStub<IGosuFieldProperty> {
  String[] getAnnotations();

  @NotNull
  String[] getNamedParameters();

  boolean isDeprecatedByDocTag();

  byte getFlags();
}
