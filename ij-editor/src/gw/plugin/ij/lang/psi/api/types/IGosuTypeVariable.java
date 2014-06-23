/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.types;

import com.intellij.psi.PsiTypeParameter;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;

/**
 */
public interface IGosuTypeVariable extends IGosuTypeDefinition, PsiTypeParameter {
  public static final IGosuTypeVariable[] EMPTY_ARRAY = new IGosuTypeVariable[0];
}
