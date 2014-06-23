/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.util;

import gw.plugin.ij.lang.psi.api.statements.arguments.IGosuNamedArgument;
import org.jetbrains.annotations.NotNull;

public interface GosuNamedArgumentsOwner {
  @NotNull
  IGosuNamedArgument[] getNamedArguments();

  @NotNull
  IGosuNamedArgument findNamedArgument(String label);
}
