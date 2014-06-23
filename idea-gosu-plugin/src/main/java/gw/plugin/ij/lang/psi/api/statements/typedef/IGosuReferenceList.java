/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.typedef;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.StubBasedPsiElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.stubs.GosuReferenceListStub;
import org.jetbrains.annotations.NotNull;

public interface IGosuReferenceList extends IGosuPsiElement, StubBasedPsiElement<GosuReferenceListStub> {
  @NotNull
  IGosuCodeReferenceElement[] getReferenceElements();

  @NotNull
  PsiClassType[] getReferenceTypes();
}
