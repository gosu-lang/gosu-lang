/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.types;

import gw.plugin.ij.lang.psi.api.expressions.IGosuReferenceExpression;
import org.jetbrains.annotations.Nullable;

public interface IGosuCodeReferenceElement extends IGosuReferenceExpression {
  IGosuCodeReferenceElement[] EMPTY_ARRAY = new IGosuCodeReferenceElement[0];

  @Nullable
  IGosuCodeReferenceElement getQualifier();

  void setQualifier(IGosuCodeReferenceElement newQualifier);
}
