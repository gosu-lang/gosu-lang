/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs;

import com.intellij.psi.stubs.NamedStub;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import org.jetbrains.annotations.NotNull;

public interface GosuMethodStub extends NamedStub<IGosuMethod> {
  String[] getAnnotations();

  @NotNull
  String[] getNamedParameters();
}
