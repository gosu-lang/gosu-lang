/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.stubs.StubElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuReferenceList;

public interface GosuReferenceListStub extends StubElement<IGosuReferenceList> {

  String[] getBaseClasses();

  PsiClassType[] getReferencedTypes();
}
