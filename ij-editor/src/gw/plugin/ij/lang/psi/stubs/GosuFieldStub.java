/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs;

import com.intellij.psi.stubs.NamedStub;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import org.jetbrains.annotations.NotNull;

public interface GosuFieldStub extends NamedStub<IGosuField> {
  String[] getAnnotations();

  boolean isEnumConstant();

  @NotNull
  String[] getNamedParameters();

  boolean isDeprecatedByDocTag();

  byte getFlags();
}
