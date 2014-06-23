/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.typedef;

import com.intellij.psi.PsiMember;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import org.jetbrains.annotations.Nullable;

public interface IGosuMember extends PsiMember, IGosuPsiElement {
  IGosuMember[] EMPTY_ARRAY = new IGosuMember[0];

  @Nullable
  IGosuModifierList getModifierList();
}
