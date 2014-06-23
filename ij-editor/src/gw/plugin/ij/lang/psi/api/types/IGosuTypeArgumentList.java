/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.types;

import gw.plugin.ij.lang.psi.IGosuPsiElement;
import org.jetbrains.annotations.NotNull;

public interface IGosuTypeArgumentList extends IGosuPsiElement {
  @NotNull
  IGosuTypeElement[] getTypeArgumentElements();
}
