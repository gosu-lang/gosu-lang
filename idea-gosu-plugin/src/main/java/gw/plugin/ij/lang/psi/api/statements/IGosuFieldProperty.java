/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements;

import com.intellij.psi.PsiField;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.util.ArrayFactory;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMember;
import gw.plugin.ij.lang.psi.stubs.GosuFieldPropertyStub;
import org.jetbrains.annotations.NotNull;

public interface IGosuFieldProperty extends IGosuMember, PsiField, StubBasedPsiElement<GosuFieldPropertyStub> {
  IGosuFieldProperty[] EMPTY_ARRAY = new IGosuFieldProperty[0];

  ArrayFactory<IGosuFieldProperty> ARRAY_FACTORY = new ArrayFactory<IGosuFieldProperty>() {
    @NotNull
    public IGosuFieldProperty[] create(final int count) {
      return count == 0 ? EMPTY_ARRAY : new IGosuFieldProperty[count];
    }
  };
}
