/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.params;

import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiParameter;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import org.jetbrains.annotations.Nullable;

public interface IGosuParameter extends PsiParameter, IGosuVariable {
  IGosuParameter[] EMPTY_ARRAY = new IGosuParameter[0];

  @Nullable
  IGosuTypeElement getTypeElementGosu();

  @Nullable
  IGosuPsiElement getDefaultInitializer();

  PsiModifierList getModifierList();

  boolean isOptional();
}
