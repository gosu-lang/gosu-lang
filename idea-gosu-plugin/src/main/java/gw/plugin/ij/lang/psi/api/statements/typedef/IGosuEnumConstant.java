/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.typedef;

import com.intellij.psi.PsiEnumConstant;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;

public interface IGosuEnumConstant extends IGosuField, PsiEnumConstant {
  IGosuEnumConstant[] EMPTY_ARRAY = new IGosuEnumConstant[0];
}
