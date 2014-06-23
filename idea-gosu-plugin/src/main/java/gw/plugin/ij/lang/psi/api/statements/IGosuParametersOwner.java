/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements;

import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;

public interface IGosuParametersOwner extends IGosuPsiElement {
  IGosuParameter[] getParameters();
}
