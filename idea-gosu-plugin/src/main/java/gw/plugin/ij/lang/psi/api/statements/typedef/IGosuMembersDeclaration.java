/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.typedef;

import com.intellij.psi.PsiModifierListOwner;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;

public interface IGosuMembersDeclaration extends IGosuPsiElement, PsiModifierListOwner {
  IGosuMembersDeclaration[] EMPTY_ARRAY = new IGosuMembersDeclaration[0];

  IGosuMember[] getMembers();

  IGosuModifierList getModifierList();
}
