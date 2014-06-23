/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api;

import gw.plugin.ij.lang.psi.api.statements.IGosuStatement;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;

public interface IGosuPackageDefinition extends IGosuStatement {
  String getPackageName();

  IGosuCodeReferenceElement getPackageReference();
}
