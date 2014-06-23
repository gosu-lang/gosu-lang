/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.typedef;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterListOwner;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;

public interface IGosuEnhancementDefinition extends IGosuTypeDefinition, IGosuTypeParameterListOwner, StubBasedPsiElement<GosuTypeDefinitionStub> {
  PsiElement getEnhancedType();
}
